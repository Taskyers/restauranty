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
import pl.taskyers.restauranty.service.reservation.ClientReservationService;

import java.util.List;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.Reservation.RESERVATION_CREATED;
import static pl.taskyers.restauranty.core.messages.enums.MessageType.SUCCESS;
import static pl.taskyers.restauranty.web.util.UriUtils.createURIFromId;

@RestController
@RequestMapping(ClientReservationService.PREFIX)
@RequiredArgsConstructor
public class ClientReservationController {
    
    final ClientReservationService clientReservationService;
    
    @PostMapping
    public ResponseEntity<ResponseMessage<ReservationDTO>> makeNewReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation saved = clientReservationService.makeReservation(reservationDTO);
        
        ReservationDTO responseReservation = ReservationDTOConverter.convertToDTO(saved);
        
        return ResponseEntity.created(createURIFromId(saved.getId()))
                .body(new ResponseMessage<>(getMessage(RESERVATION_CREATED), SUCCESS,
                        responseReservation));
    }
    
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        
        List<Reservation> toFind = clientReservationService.getClientReservations();
        
        return ResponseEntity.ok(ReservationDTOConverter.convertToDTOList(toFind));
    }
    
    @PutMapping(ClientReservationService.CANCEL)
    public ResponseEntity<ResponseMessage<ReservationDTO>> cancelReservation(@PathVariable final Long id) {
        Reservation canceled = clientReservationService.cancelReservation(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Reservation.RESERVATION_CANCELED, MessageType.SUCCESS,
                ReservationDTOConverter.convertToDTO(canceled)));
    }
    
}
