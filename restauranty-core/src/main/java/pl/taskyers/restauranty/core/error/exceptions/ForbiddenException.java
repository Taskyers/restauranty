package pl.taskyers.restauranty.core.error.exceptions;

public class ForbiddenException extends RuntimeException implements Error {
    
    private static final long serialVersionUID = -8295805198982544154L;
    
    public ForbiddenException(String message) {
        super(message);
    }
    
    @Override
    public String getBasicMessage() {
        return "Forbidden";
    }
}
