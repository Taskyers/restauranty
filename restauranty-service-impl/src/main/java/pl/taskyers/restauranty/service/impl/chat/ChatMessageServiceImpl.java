package pl.taskyers.restauranty.service.impl.chat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.chat.ChatMessageNotFoundException;
import pl.taskyers.restauranty.core.data.chat.converters.ChatMessageConverter;
import pl.taskyers.restauranty.core.data.chat.dto.ChatMessageDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.MessageStatus;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.chat.ChatMessageRepository;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.chat.ChatMessageService;
import pl.taskyers.restauranty.service.chat.ChatRoomService;
import pl.taskyers.restauranty.service.impl.chat.validator.ChatMessageValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    
    private final ChatMessageRepository chatMessageRepository;
    
    private final UserRepository userRepository;
    
    private final ChatRoomService chatRoomService;
    
    private final AuthProvider authProvider;
    
    @Override
    public ChatMessage sendMessage(@NonNull ChatMessageDTO messageDTO) {
        
        if ( !authProvider.getUserEntity().getUsername().equals(messageDTO.getAuthor()) ) {
            throw new ForbiddenException(MessageCode.ChatMessage.AUTHOR_IS_NOT_YOU);
        }
        
        UserBase userAuthor = authProvider.getUserEntity();
        UserBase userRecipient = userRepository.findByUsername(messageDTO.getRecipient()).orElseThrow(() -> new ChatMessageNotFoundException(
                MessageProvider.getMessage(MessageCode.ChatMessage.RECIPIENT_NOT_FOUND, "username", messageDTO.getRecipient())));
        
        validate(userAuthor.getRole().getRole(), userRecipient.getRole().getRole(), messageDTO.getContent());
        
        ChatMessage chatMessage = ChatMessage
                .builder()
                .author(userAuthor)
                .recipient(userRecipient)
                .chatRoom(userAuthor.getRole().getRole() == RoleType.ROLE_CLIENT ? chatRoomService.getChatRoom(userAuthor, userRecipient).get() :
                        chatRoomService.getChatRoom(userRecipient, userAuthor).get())
                .timestamp(new Date())
                .content(messageDTO.getContent())
                .status(MessageStatus.RECEIVED)
                .build();
        
        return chatMessageRepository.save(chatMessage);
    }
    
    @Override
    public List<ChatMessageDTO> getChatMessages(@NonNull String recipient) {
        
        UserBase userAuthor = authProvider.getUserEntity();
        UserBase userRecipient = userRepository.findByUsername(recipient).orElseThrow(() -> new ChatMessageNotFoundException(
                MessageProvider.getMessage(MessageCode.ChatMessage.RECIPIENT_NOT_FOUND, "username", recipient)));
        
        List<ChatMessage> messages = getSortedChatMessages(userAuthor, userRecipient);
        
        if ( messages.size() > 0 ) {
            for ( ChatMessage message : messages ) {
                message.setStatus(MessageStatus.DELIVERED);
                chatMessageRepository.save(message);
            }
        }
        
        return ChatMessageConverter.convertToDTOList(messages);
    }
    
    @Override
    public Long countNewMessages(@NonNull String author) {
        UserBase userRecipient = authProvider.getUserEntity();
        UserBase userAuthor = userRepository.findByUsername(author).orElseThrow(() -> new ChatMessageNotFoundException(
                MessageProvider.getMessage(MessageCode.ChatMessage.AUTHOR_NOT_FOUND, "username", author)));
        return chatMessageRepository.countByAuthorAndRecipientAndStatus(userAuthor, userRecipient, MessageStatus.RECEIVED);
    }
    
    private List<ChatMessage> getSortedChatMessages(UserBase userAuthor, UserBase userRecipient) {
        List<ChatMessage> messages = userAuthor.getRole().getRole() == RoleType.ROLE_CLIENT ?
                new ArrayList<>(chatRoomService.getChatRoomMessages(userAuthor, userRecipient)) :
                new ArrayList<>(chatRoomService.getChatRoomMessages(userRecipient, userAuthor));
        messages.sort(Comparator.comparing(ChatMessage::getTimestamp));
        return messages;
    }
    
    private void validate(RoleType authorRole, RoleType recipientRole, String content) {
        final ValidationMessageContainer validationMessageContainer = ChatMessageValidator.validate(authorRole, recipientRole, content);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
    }
    
}
