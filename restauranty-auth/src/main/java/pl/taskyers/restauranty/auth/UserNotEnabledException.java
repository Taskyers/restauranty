package pl.taskyers.restauranty.auth;

public class UserNotEnabledException extends RuntimeException {
    
    private static final long serialVersionUID = -2941730820393792022L;
    
    public UserNotEnabledException(String message) {
        super(message);
    }
    
}
