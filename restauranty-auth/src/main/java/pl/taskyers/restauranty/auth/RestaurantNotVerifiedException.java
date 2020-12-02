package pl.taskyers.restauranty.auth;

public class RestaurantNotVerifiedException extends RuntimeException {
    
    private static final long serialVersionUID = 7087917493714925210L;
    
    public RestaurantNotVerifiedException(String message) {
        super(message);
    }
    
}
