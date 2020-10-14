package pl.taskyers.restauranty.core.messages;

import lombok.Getter;
import pl.taskyers.restauranty.core.messages.enums.MessageType;

@Getter
public class ValidationMessage extends Message {
    
    private final String field;
    
    public ValidationMessage(String message, MessageType messageType, String field) {
        super(message, messageType);
        this.field = field;
    }
    
}
