package pl.taskyers.restauranty.service.reservation;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;

import java.util.List;

public interface RestaurantReservationService {
    
    String PREFIX = "/restaurant/reservations";
    
    String BY_ID = "/{id}";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    String ACCEPT="/accept/{id}";
    
    String REJECT = "/reject/{id}";
    
    List<ReservationDTO> getAllRestaurantReservations(@NonNull final String restaurantName);
    
    Reservation acceptReservation(@NonNull final Long id);
    
    Reservation rejectReservation(@NonNull final Long id);
    
    
}
