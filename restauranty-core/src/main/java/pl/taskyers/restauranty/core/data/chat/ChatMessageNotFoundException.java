package pl.taskyers.restauranty.core.data.chat;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ChatMessageNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = 3748987874228416796L;
    
    public ChatMessageNotFoundException(String message) {
        super(message);
    }
    
}
