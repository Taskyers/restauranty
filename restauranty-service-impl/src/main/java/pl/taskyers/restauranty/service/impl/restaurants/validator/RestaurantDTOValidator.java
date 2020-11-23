package pl.taskyers.restauranty.service.impl.restaurants.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.utils.ValidationUtils;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.impl.addresses.validator.AddressDTOValidator;

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
    
    private final RestaurantRepository restaurantRepository;
    
    private final AddressDTOValidator addressDTOValidator;
    
    public ValidationMessageContainer validate(RestaurantDTO restaurantDTO, boolean checkForDuplicates) {
        final ValidationMessageContainer validationMessageContainer = addressDTOValidator.validate(restaurantDTO.getAddress());
        validateName(restaurantDTO.getName(), validationMessageContainer, checkForDuplicates);
        validateDescription(restaurantDTO.getDescription(), validationMessageContainer);
        validatePhoneNumber(restaurantDTO.getPhoneNumber(), validationMessageContainer, checkForDuplicates);
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
    
}
