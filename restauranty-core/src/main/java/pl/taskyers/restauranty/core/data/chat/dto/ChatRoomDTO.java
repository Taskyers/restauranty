package pl.taskyers.restauranty.core.data.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    
    private Long id;
    
    private String client;
    
    private String restaurant;
    
}