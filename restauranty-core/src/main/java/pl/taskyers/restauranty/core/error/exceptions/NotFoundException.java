package pl.taskyers.restauranty.core.error.exceptions;

/**
 * Class for mapping 404 error on {@link pl.taskyers.restauranty.web.handler.RestExceptionHandler}
 */
public abstract class NotFoundException extends RuntimeException implements Error {
    
    private static final long serialVersionUID = 2657601476061418343L;
    
    public NotFoundException(String message) {
        super(message);
    }
    
    @Override
    public String getBasicMessage() {
        return "Resource not found";
    }
    
}
