package pl.taskyers.restauranty.core.data.restaurants;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class RestaurantNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = 2722172033500171842L;
    
    public RestaurantNotFoundException(String message) {
        super(message);
    }
    
}
