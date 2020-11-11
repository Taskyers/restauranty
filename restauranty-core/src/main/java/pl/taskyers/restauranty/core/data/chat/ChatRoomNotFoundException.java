package pl.taskyers.restauranty.core.data.chat;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ChatRoomNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = 1141056756676691289L;
    
    public ChatRoomNotFoundException(String message) {
        super(message);
    }
    
}
