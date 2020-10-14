package pl.taskyers.restauranty.core.error.exceptions;

/**
 * Class for mapping 404 error on <link restHandler>
 */
public abstract class NotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 2657601476061418343L;
    
    public static final String MESSAGE = "Not found";
    
    public NotFoundException(String message) {
        super(message);
    }
    
}
