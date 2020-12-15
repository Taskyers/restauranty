package pl.taskyers.restauranty.service.impl.reservation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.openhour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.reservation.ReservationNotFound;
import pl.taskyers.restauranty.core.data.reservation.converters.ReservationDTOConverter;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.reservation.ReservationRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.reservation.validator.ReservationDTOValidator;
import pl.taskyers.restauranty.service.reservation.ClientReservationService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;

@Service
@RequiredArgsConstructor
public class ClientReservationServiceImpl implements ClientReservationService {
    
    private static final double ALLOWED_PERCENTAGE_OF_RESTAURANT_CAPACITY = 0.8; // 80% during one hour
    
    private final ReservationDTOValidator reservationDTOValidator;
    
    private final AuthProvider authProvider;
    
    private final ReservationRepository reservationRepository;
    
    private final RestaurantRepository restaurantRepository;
    
    @Override
    public Reservation makeReservation(@NonNull ReservationDTO reservationDTO) {
        UserClient client = (UserClient) authProvider.getUserEntity();
        ValidationMessageContainer validationMessageContainer = reservationDTOValidator.validate(reservationDTO, false);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        Restaurant restaurant =
                restaurantRepository.findByName(reservationDTO.getRestaurantName()).orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "name",
                                reservationDTO.getRestaurantName())));
        Reservation toSave = ReservationDTOConverter.convertFromDTO(reservationDTO, client, restaurant);
        
        if ( !isAbleToMakeReservation(toSave, validationMessageContainer) ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        return reservationRepository.save(toSave);
    }
    
    @Override
    public List<Reservation> getClientReservations() {
        UserClient client = (UserClient) authProvider.getUserEntity();
        return reservationRepository.findAllByClient(client);
    }
    
    @Override
    public Reservation cancelReservation(@NonNull Long id) {
        final Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFound(MessageProvider.getMessage(MessageCode.Reservation.RESERVATION_NOT_FOUND, "id", id)));
        if ( !reservation.getClient()
                .getUsername()
                .equals(authProvider.getUserLogin()) ) {
            throw new ForbiddenException(MessageCode.Reservation.RESERVATION_NOT_YOURS);
        }
        
        reservation.setStatus(ReservationStatus.CANCELED);
        
        return reservationRepository.save(reservation);
    }
    
    private boolean isAbleToMakeReservation(Reservation reservation, ValidationMessageContainer validationMessageContainer) {
        Date truncatedStartTime = DateUtils.truncate(reservation.getReservationTime(), Calendar.HOUR);
        Date truncatedEndTime = DateUtils.addHours(truncatedStartTime, 1);
        
        if ( reservation.getRestaurant().getCapacity() * ALLOWED_PERCENTAGE_OF_RESTAURANT_CAPACITY <
             reservationRepository.countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(reservation.getRestaurant(),
                     ReservationStatus.ACCEPTED, reservation.getReservationDate(), truncatedStartTime, truncatedEndTime
             ) ) {
            validationMessageContainer.addError(getMessage(MessageCode.Reservation.RESERVATION_NOT_ALLOWED, "Reservation"),
                    "personsCount, reservationTime");
            return false;
        } else if ( reservation.getPersonsCount() > reservation.getRestaurant().getCapacity() ) {
            validationMessageContainer.addError(getMessage(MessageCode.FIELD_INVALID_FORMAT, "Persons count"), "personsCount");
            return false;
        } else if ( !isReservationTimeBetweenOpenHours(reservation) ) {
            validationMessageContainer.addError(getMessage(MessageCode.Reservation.RESERVATION_INVALID_TIME, "Reservation time"), "reservationTime");
            return false;
        }
        return true;
    }
    
    private boolean isReservationTimeBetweenOpenHours(Reservation reservation) {
        DayOfWeek weekDay = LocalDate.parse(pl.taskyers.restauranty.core.utils.DateUtils.parseStringDate(reservation.getReservationDate()),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).getDayOfWeek();
        
        List<OpenHour> openHours = reservation.getRestaurant()
                .getOpenHours()
                .stream()
                .filter(openHour -> openHour.getDayOfWeek() == weekDay.getValue())
                .collect(Collectors.toList());
        if ( openHours.size() > 0 ) {
            for ( OpenHour openHour : openHours ) {
                if ( reservation.getReservationTime().before(openHour.getCloseTime()) &&
                     reservation.getReservationTime().after(openHour.getOpenTime()) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
