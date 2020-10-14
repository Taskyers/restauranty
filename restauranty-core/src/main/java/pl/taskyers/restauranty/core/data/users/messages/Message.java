package pl.taskyers.restauranty.core.data.users.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Message {
    
    private String message;
    
    private MessageType type;
    
}
