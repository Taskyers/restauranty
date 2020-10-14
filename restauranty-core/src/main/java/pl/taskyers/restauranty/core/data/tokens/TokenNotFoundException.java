package pl.taskyers.restauranty.core.data.tokens;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class TokenNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -3333235527969743290L;
    
    public TokenNotFoundException(String message) {
        super(message);
    }
    
}
