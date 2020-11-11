package pl.taskyers.restauranty.core.data.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    
    private String author;
    
    private String recipient;
    
    private String content;
    
    private String timestamp;
    
}
