package pl.taskyers.restauranty.core.data.reviews;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -1139702657520108215L;
    
    public ReviewNotFoundException(String message) {
        super(message);
    }
    
}
