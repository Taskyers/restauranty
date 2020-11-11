package pl.taskyers.restauranty.service.impl.chat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.chat.ChatRoomNotFoundException;
import pl.taskyers.restauranty.core.data.chat.converters.ChatRoomConverter;
import pl.taskyers.restauranty.core.data.chat.dto.ChatRoomDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.chat.ChatRoomRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.chat.ChatRoomService;
import pl.taskyers.restauranty.service.impl.chat.validator.ChatRoomValidator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    
    private final ChatRoomRepository chatRoomRepository;
    
    private final AuthProvider authProvider;
    
    @Override
    public Optional<ChatRoom> getChatRoom(@NonNull UserBase userClient, @NonNull UserBase userRestaurant) {
        validate(userClient.getRole().getRole(), userRestaurant.getRole().getRole());
        
        return chatRoomRepository.findByClientAndRestaurant(userClient, userRestaurant).or(() -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setClient(userClient);
            chatRoom.setRestaurant(userRestaurant);
            
            return Optional.of(chatRoomRepository.save(chatRoom));
        });
    }
    
    @Override
    public Set<ChatMessage> getChatRoomMessages(@NonNull UserBase client, @NonNull UserBase restaurant) {
        if ( !chatRoomRepository.findByClientAndRestaurant(client, restaurant).isPresent() ) {
            throw new ChatRoomNotFoundException(
                    MessageProvider.getMessage(MessageCode.ChatRoom.CHAT_ROOM_NOT_FOUND, client.getUsername(), restaurant.getUsername()));
        }
        return chatRoomRepository.findByClientAndRestaurant(client, restaurant).get().getMessages();
    }
    
    @Override
    public List<ChatRoomDTO> getCurrentUserChatRooms() {
        UserBase userBase = authProvider.getUserEntity();
        return ChatRoomConverter.convertToDTOList(chatRoomRepository.findChatsByUserId(userBase.getId()));
    }
    
    private void validate(RoleType authorRole, RoleType recipientRole) {
        final ValidationMessageContainer validationMessageContainer = ChatRoomValidator.validate(authorRole, recipientRole);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
    }
    
}
