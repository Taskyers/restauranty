package pl.taskyers.restauranty.core.data.chat.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.chat.dto.ChatRoomDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ChatRoomConverter {
    
    public List<ChatRoomDTO> convertToDTOList(List<ChatRoom> chatRooms) {
        List<ChatRoomDTO> result = new ArrayList<>(chatRooms.size());
        for ( ChatRoom chatRoom : chatRooms ) {
            result.add(new ChatRoomDTO(chatRoom.getId(), chatRoom.getClient().getUsername(), chatRoom.getRestaurant().getUsername()));
        }
        return result;
    }
    
}
