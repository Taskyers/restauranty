package pl.taskyers.restauranty.core.data.users.messages;

import lombok.Getter;

@Getter
public class ValidationMessage extends Message {
    
    private String field;
    
    public ValidationMessage(String message, MessageType messageType, String field) {
        super(message, messageType);
        this.field = field;
    }
    
}
