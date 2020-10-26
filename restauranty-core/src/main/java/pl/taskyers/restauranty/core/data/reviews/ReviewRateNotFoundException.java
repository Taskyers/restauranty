package pl.taskyers.restauranty.core.data.reviews;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ReviewRateNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -8937245257986965672L;
    
    public ReviewRateNotFoundException(String message) {
        super(message);
    }
    
}
