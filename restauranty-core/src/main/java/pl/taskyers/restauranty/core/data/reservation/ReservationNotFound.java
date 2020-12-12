package pl.taskyers.restauranty.core.data.reservation;

import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;

public class ReservationNotFound extends NotFoundException {
    
    private static final long serialVersionUID = -1031046106179475538L;
    
    public ReservationNotFound(String message) {
        super(message);
    }
    
}
