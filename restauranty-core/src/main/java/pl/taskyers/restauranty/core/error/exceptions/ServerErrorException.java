package pl.taskyers.restauranty.core.error.exceptions;

/**
 * Class for mapping 500 error on {@link pl.taskyers.restauranty.web.handler.RestExceptionHandler}
 */
public class ServerErrorException extends RuntimeException implements Error {
    
    private static final long serialVersionUID = 3709081953024455326L;
    
    public ServerErrorException(String message) {
        super(message);
    }
    
    @Override
    public String getBasicMessage() {
        return "Error occurred on server";
    }
    
}
