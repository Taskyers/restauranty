package pl.taskyers.restauranty.core.data.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    
    private long id;
    
    private String reservationDate;
    
    private String reservationTime;
    
    private int personsCount;
    
    private String restaurantName;
    
    private ReservationStatus status;
    
    private String clientUsername;
    
}
