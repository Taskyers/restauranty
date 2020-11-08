package pl.taskyers.restauranty.core.data.reviews;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ReviewReportNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -8237701926606404114L;
    
    public ReviewReportNotFoundException(String message) {
        super(message);
    }
    
}
