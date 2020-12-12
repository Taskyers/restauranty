package pl.taskyers.restauranty.service.impl.open_hour.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.utils.DateUtils;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_EMPTY;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_INVALID_FORMAT;

@Component
public class OpenHourDTOValidator {
    
    private static final String OPEN_TIME = "openTime";
    
    private static final String CLOSE_TIME = "closeTime";
    
    private static final String DAY_OF_WEEK = "dayOfWeek";
    
    public void validate(OpenHourDTO openHourDTO, ValidationMessageContainer validationMessageContainer) {
        validateDayOfWeek(openHourDTO.getDayOfWeek(), validationMessageContainer);
        validateOpenAndCloseTime(openHourDTO.getOpenTime(), openHourDTO.getCloseTime(), validationMessageContainer);
    }
    
    private void validateOpenAndCloseTime(String openTime, String closeTime, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(openTime) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Open time"), OPEN_TIME);
        } else if ( StringUtils.isBlank(closeTime) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Close time"), CLOSE_TIME);
        } else if ( DateUtils.parseTime(openTime) == null ) {
            validationMessageContainer.addError(getMessage(MessageCode.FIELD_INVALID_FORMAT, "Open time"), OPEN_TIME);
        } else if ( DateUtils.parseTime(closeTime) == null ) {
            validationMessageContainer.addError(getMessage(MessageCode.FIELD_INVALID_FORMAT, "Close time"), CLOSE_TIME);
        } else if ( Objects.requireNonNull(DateUtils.parseTime(openTime)).after(Objects.requireNonNull(DateUtils.parseTime(closeTime))) ) {
            validationMessageContainer.addError(getMessage(MessageCode.OpenHour.OPEN_TIME_AFTER_CLOSE_TIME, "Open time"), OPEN_TIME);
        }
    }
    
    private void validateDayOfWeek(String dayOfWeek, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(dayOfWeek) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Day of week"), DAY_OF_WEEK);
        } else if ( !isDayOfWeekNameValid(dayOfWeek) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "Day of week"), DAY_OF_WEEK);
        }
    }
    
    private boolean isDayOfWeekNameValid(String dayOfWeek) {
        for ( String dayName : Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).collect(Collectors.toList()) ) {
            if ( dayOfWeek.toUpperCase().equals(dayName) ) {
                return true;
            }
        }
        return false;
    }
    
}
