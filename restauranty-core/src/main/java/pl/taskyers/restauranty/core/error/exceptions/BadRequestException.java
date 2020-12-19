package pl.taskyers.restauranty.core.error.exceptions;

import lombok.Getter;
import pl.taskyers.restauranty.core.messages.Message;

import java.util.List;

/**
 * Class for mapping 400 error on {@link pl.taskyers.restauranty.web.handler.RestExceptionHandler}
 */
@Getter
public abstract class BadRequestException extends RuntimeException implements Error {
    
    private static final long serialVersionUID = -7617832958211047285L;
    
    protected List<Message> messages;
    
    @Override
    public String getBasicMessage() {
        return "Bad request";
    }
    
}
