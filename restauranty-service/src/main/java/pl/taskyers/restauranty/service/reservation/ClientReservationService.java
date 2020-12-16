package pl.taskyers.restauranty.service.reservation;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;

import java.util.List;

public interface ClientReservationService {
    
    String PREFIX = "/client/reservations";
    
    String CANCEL = "/cancel/{id}";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    
    Reservation makeReservation(@NonNull ReservationDTO reservationDTO);
    
    List<Reservation> getClientReservations();
    
    Reservation cancelReservation(@NonNull final Long id);
    
}
