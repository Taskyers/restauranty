package pl.taskyers.restauranty.core.error.exceptions;

import pl.taskyers.restauranty.core.messages.Message;

import java.util.List;

public class ValidationException extends BadRequestException {
    
    private static final long serialVersionUID = 6177093623757762863L;
    
    public ValidationException(List<Message> messages) {
        this.messages = messages;
    }
    
}
