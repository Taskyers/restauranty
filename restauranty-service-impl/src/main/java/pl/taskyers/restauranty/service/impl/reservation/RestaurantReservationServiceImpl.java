package pl.taskyers.restauranty.service.impl.reservation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.reservation.ReservationNotFound;
import pl.taskyers.restauranty.core.data.reservation.converters.ReservationDTOConverter;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.reservation.ReservationRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.reservation.RestaurantReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantReservationServiceImpl implements RestaurantReservationService {
    
    final AuthProvider authProvider;
    
    final RestaurantRepository restaurantRepository;
    
    final ReservationRepository reservationRepository;
    
    @Override
    public List<ReservationDTO> getAllRestaurantReservations(@NonNull String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName).orElseThrow(() -> new RestaurantNotFoundException(
                MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "name", restaurantName)));
        if ( !restaurant.getOwner().getUsername().equals(authProvider.getUserLogin()) ) {
            throw new ForbiddenException(MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_YOURS));
        }
        
        return ReservationDTOConverter.convertToDTOList(reservationRepository.findAllByRestaurant(restaurant));
    }
    
    @Override
    public Reservation acceptReservation(@NonNull Long id) {
        return changeReservationStatus(id, ReservationStatus.ACCEPTED);
    }
    
    @Override
    public Reservation rejectReservation(@NonNull Long id) {
        return changeReservationStatus(id, ReservationStatus.REJECTED);
    }
    
    private Reservation changeReservationStatus(Long id, ReservationStatus status) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFound(MessageProvider.getMessage(MessageCode.Reservation.RESERVATION_NOT_FOUND, "id", id)));
        if ( !reservation.getRestaurant().getOwner()
                .getUsername()
                .equals(authProvider.getUserLogin()) ) {
            throw new ForbiddenException(MessageCode.Reservation.RESERVATION_NOT_YOURS);
        }
        
        reservation.setStatus(status);
        
        return reservationRepository.save(reservation);
    }
    
}
