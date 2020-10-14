package pl.taskyers.restauranty.core.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.taskyers.restauranty.core.messages.enums.MessageType;

@Getter
@AllArgsConstructor
public abstract class Message {
    
    private final String message;
    
    private final MessageType type;
    
}
