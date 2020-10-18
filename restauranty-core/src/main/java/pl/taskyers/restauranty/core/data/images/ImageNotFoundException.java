package pl.taskyers.restauranty.core.data.images;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ImageNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -2278153037369019639L;
    
    public ImageNotFoundException(String message) {
        super(message);
    }
    
}
