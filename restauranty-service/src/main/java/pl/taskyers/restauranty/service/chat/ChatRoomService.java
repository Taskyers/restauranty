package pl.taskyers.restauranty.service.chat;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.chat.dto.ChatRoomDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomService {
    
    String CHAT_ROOM_PREFIX = "/secured/chats";
    
    Optional<ChatRoom> getChatRoom(@NonNull UserBase client, @NonNull UserBase restaurant);
    
    Set<ChatMessage> getChatRoomMessages(@NonNull UserBase client, @NonNull UserBase restaurant);
    
    List<ChatRoomDTO> getCurrentUserChatRooms();
    
}
