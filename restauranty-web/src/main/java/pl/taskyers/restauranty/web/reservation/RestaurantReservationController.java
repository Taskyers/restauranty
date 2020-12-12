package pl.taskyers.restauranty.web.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.reservation.converters.ReservationDTOConverter;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.reservation.RestaurantReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestaurantReservationService.PREFIX)
public class RestaurantReservationController {
    
    private final RestaurantReservationService restaurantReservationService;
    
    @GetMapping(RestaurantReservationService.BY_RESTAURANT)
    public ResponseEntity<List<ReservationDTO>> getAllRestaurantReservations(@PathVariable final String restaurant) {
        List<ReservationDTO> toFind = restaurantReservationService.getAllRestaurantReservations(restaurant);
        
        return ResponseEntity.ok(toFind);
    }
    
    @PutMapping(RestaurantReservationService.ACCEPT)
    public ResponseEntity<ResponseMessage<ReservationDTO>> acceptReservation(@PathVariable final Long id) {
        Reservation accepted = restaurantReservationService.acceptReservation(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Reservation.RESERVATION_ACCEPTED, MessageType.SUCCESS,
                ReservationDTOConverter.convertToDTO(accepted)));
    }
    
    @PutMapping(RestaurantReservationService.REJECT)
    public ResponseEntity<ResponseMessage<ReservationDTO>> rejectReservation(@PathVariable final Long id) {
        Reservation rejected = restaurantReservationService.rejectReservation(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Reservation.RESERVATION_REJECTED, MessageType.SUCCESS,
                ReservationDTOConverter.convertToDTO(rejected)));
    }
    
}
