package pl.taskyers.restauranty.service.impl.reservation.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.utils.DateUtils;

import java.util.Date;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_EMPTY;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_INVALID_FORMAT;

@Component
public class ReservationDTOValidator {
    
    public ValidationMessageContainer validate(ReservationDTO reservationDTO, boolean isRestaurant) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        validateDate(reservationDTO.getReservationDate(), validationMessageContainer);
        validateTime(reservationDTO.getReservationTime(), validationMessageContainer);
        validateRestaurantName(reservationDTO.getRestaurantName(), validationMessageContainer);
        if ( isRestaurant ) {
            validateUsername(reservationDTO.getClientUsername(), validationMessageContainer);
        }
        validatePersonsCount(reservationDTO.getPersonsCount(), validationMessageContainer);
        return validationMessageContainer;
        
    }
    
    private void validateDate(String reservationDate, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(reservationDate) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Reservation date"), "reservationDate");
        } else if ( DateUtils.parseDate(reservationDate) == null || DateUtils.parseDate(reservationDate).before(new Date()) ) {
            validationMessageContainer.addError(getMessage(MessageCode.FIELD_INVALID_FORMAT, "Reservation date"), "reservationDate");
        }
    }
    
    private void validateTime(String reservationTime, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(reservationTime) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Reservation time"), "reservationTime");
        } else if ( DateUtils.parseTime(reservationTime) == null || !isTimeValid(reservationTime)) {
            validationMessageContainer.addError(getMessage(MessageCode.FIELD_INVALID_FORMAT, "Reservation time"), "reservationTime");
        }
        
    }
    
    private void validateUsername(String clientUsername, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(clientUsername) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Client username"), "clientUsername");
        }
    }
    
    private void validatePersonsCount(int personsCount, ValidationMessageContainer validationMessageContainer) {
        if ( personsCount <= 0 ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "Persons count"), "personsCount");
        }
    }
    
    private void validateRestaurantName(String restaurantName, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(restaurantName) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Restaurant name"), "restaurantName");
        }
    }
    
    private boolean isTimeValid(String reservationTime) {
        int hour = Integer.parseInt(reservationTime.split(":")[0]);
        int minutes = Integer.parseInt(reservationTime.split(":")[1]);
        if ( hour > 24 || hour < 0 || minutes > 60 || minutes < 0 ) {
            return false;
        }
        return true;
    }
    
}
