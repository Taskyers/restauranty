package pl.taskyers.restauranty.core.messages;

import lombok.Getter;
import pl.taskyers.restauranty.core.messages.enums.MessageType;

@Getter
public class ResponseMessage<T> extends Message {
    
    private T object;
    
    public ResponseMessage(String message, MessageType messageType, T object) {
        super(message, messageType);
        this.object = object;
    }
    
    public ResponseMessage(String message, MessageType messageType) {
        super(message, messageType);
    }
    
}
