package pl.taskyers.restauranty.core.data.addresses;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class AddressNotFoundException extends NotFoundException {
    
    private static final long serialVersionUID = -6798400008801470484L;
    
    public AddressNotFoundException(String message) {
        super(message);
    }
    
}
