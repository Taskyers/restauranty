package pl.taskyers.restauranty.core.data.users.messages;

import java.text.MessageFormat;

public class MessageProvider {
    
    public static String getMessage(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
    
}
