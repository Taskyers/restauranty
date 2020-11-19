package pl.taskyers.restauranty.core.data.menu;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class DishNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -311420950040610096L;
    
    public DishNotFoundException(String message) {
        super(message);
    }
    
}
