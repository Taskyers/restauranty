package pl.taskyers.restauranty.service.impl.restaurants.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.openhour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.utils.ValidationUtils;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.impl.addresses.validator.AddressDTOValidator;
import pl.taskyers.restauranty.service.impl.openhour.validator.OpenHourDTOValidator;

import java.util.Set;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_EMPTY;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_INVALID_FORMAT;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.Restaurant.RESTAURANT_WITH_FIELD_EXISTS;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantDTOValidator {
    
    private static final String FIELD_NAME = "name";
    
    private static final String FIELD_DESCRIPTION = "description";
    
    private static final String FIELD_PHONE_NUMBER = "phoneNumber";
    
    private static final String CAPACITY = "capacity";
    
    private final RestaurantRepository restaurantRepository;
    
    private final AddressDTOValidator addressDTOValidator;
    
    private final OpenHourDTOValidator openHourDTOValidator;
    
    public ValidationMessageContainer validate(RestaurantDTO restaurantDTO, boolean checkForDuplicates) {
        final ValidationMessageContainer validationMessageContainer = addressDTOValidator.validate(restaurantDTO.getAddress());
        validateName(restaurantDTO.getName(), validationMessageContainer, checkForDuplicates);
        validateDescription(restaurantDTO.getDescription(), validationMessageContainer);
        validatePhoneNumber(restaurantDTO.getPhoneNumber(), validationMessageContainer, checkForDuplicates);
        validateCapacity(restaurantDTO.getCapacity(), validationMessageContainer);
        validateOpenHours(restaurantDTO.getOpenHours(), validationMessageContainer);
        return validationMessageContainer;
    }
    
    private void validateName(String name, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        if ( StringUtils.isBlank(name) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Name"), FIELD_NAME);
        } else if ( checkForDuplicates && restaurantRepository.findByName(name)
                .isPresent() ) {
            String message = getMessage(RESTAURANT_WITH_FIELD_EXISTS, name, "name");
            validationMessageContainer.addError(message, FIELD_NAME);
            log.debug(message);
        }
    }
    
    private void validateDescription(String description, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(description) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Description"), FIELD_DESCRIPTION);
        }
    }
    
    private void validatePhoneNumber(String phoneNumber, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        if ( StringUtils.isBlank(phoneNumber) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Phone Number"), FIELD_PHONE_NUMBER);
        } else if ( !ValidationUtils.isPhoneNumberValid(phoneNumber) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "phoneNumber"), FIELD_PHONE_NUMBER);
        } else if ( checkForDuplicates && restaurantRepository.findByPhoneNumber(phoneNumber)
                .isPresent() ) {
            String message = getMessage(RESTAURANT_WITH_FIELD_EXISTS, phoneNumber, "phoneNumber");
            validationMessageContainer.addError(message, FIELD_PHONE_NUMBER);
            log.debug(message);
        }
    }
    
    private void validateCapacity(int capacity, ValidationMessageContainer validationMessageContainer) {
        if ( capacity <= 0 ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "capacity"), CAPACITY);
        }
    }
    
    private void validateOpenHours(Set<OpenHourDTO> openHourDTOS, ValidationMessageContainer validationMessageContainer) {
        for ( OpenHourDTO openHourDTO : openHourDTOS ) {
            openHourDTOValidator.validate(openHourDTO, validationMessageContainer);
        }
    }
    
}
