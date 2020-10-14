package pl.taskyers.restauranty.core.messages;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;

@UtilityClass
public class MessageProvider {
    
    public String getMessage(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
    
}
