package pl.taskyers.restauranty.service.chat;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.chat.dto.ChatMessageDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    
    String CHAT_MESSAGE_PREFIX = "/secured/messages";
    
    ChatMessage sendMessage(@NonNull ChatMessageDTO messageDTO);
    
    List<ChatMessageDTO> getChatMessages(@NonNull final String recipient);
    
    Long countNewMessages(@NonNull final String recipient);
    
}
