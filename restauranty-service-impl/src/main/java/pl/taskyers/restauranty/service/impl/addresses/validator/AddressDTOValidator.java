package pl.taskyers.restauranty.service.impl.addresses.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.utils.ValidationUtils;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_EMPTY;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.FIELD_INVALID_FORMAT;

@Component
@Slf4j
public class AddressDTOValidator {
    
    private static final String FIELD_STREET = "street";
    
    private static final String FIELD_ZIP_CODE = "zipCode";
    
    private static final String FIELD_CITY = "city";
    
    private static final String FIELD_COUNTRY = "country";
    
    public ValidationMessageContainer validate(AddressDTO addressDTO) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        validateStreet(addressDTO.getStreet(), validationMessageContainer);
        validateZipCode(addressDTO.getZipCode(), validationMessageContainer);
        validateCountry(addressDTO.getCountry(), validationMessageContainer);
        validateCity(addressDTO.getCity(), validationMessageContainer);
        
        return validationMessageContainer;
    }
    
    private void validateStreet(String street, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(street) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Street"), FIELD_STREET);
        }
    }
    
    private void validateZipCode(String zipCode, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(zipCode) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Zip Code"), FIELD_ZIP_CODE);
        } else if ( !ValidationUtils.isZipCodeValid(zipCode) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "zipCode"), FIELD_ZIP_CODE);
        }
    }
    
    private void validateCountry(String country, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(country) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Country"), FIELD_COUNTRY);
        } else if ( !ValidationUtils.isCountryNameValid(country) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "country"), FIELD_COUNTRY);
        }
    }
    
    private void validateCity(String city, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(city) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "City"), FIELD_CITY);
        } else if ( !ValidationUtils.isCountryNameValid(city) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "city"), FIELD_CITY);
        }
    }
    
}
