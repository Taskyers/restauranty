package pl.taskyers.restauranty.core.data.users;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -538860484607136538L;
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
