package pl.taskyers.restauranty.core.error.exceptions;

import lombok.Getter;
import pl.taskyers.restauranty.core.messages.Message;

import java.util.List;

/**
 * Class for mapping 400 error on <link restHandler>
 */
@Getter
public abstract class BadRequestException extends RuntimeException {
    
    private static final long serialVersionUID = -7617832958211047285L;
    
    public static String MESSAGE = "Bad request";
    
    protected List<Message> messages;
    
}
