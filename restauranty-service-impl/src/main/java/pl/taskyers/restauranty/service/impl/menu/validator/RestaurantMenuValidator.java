package pl.taskyers.restauranty.service.impl.menu.validator;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;

import java.util.Set;

@UtilityClass
public class RestaurantMenuValidator {
    
    private final String DESCRIPTION = "description";
    
    private final String PRICE = "price";
    
    private final String NAME = "name";
    
    public ValidationMessageContainer validate(@NonNull final AddDishDTO dishToAdd) {
        return validateWithoutCheckForDuplicates(dishToAdd);
    }
    
    public ValidationMessageContainer validateWithDuplicates(@NonNull final Set<SingleMenuDish> existingMenu, @NonNull final AddDishDTO dishToAdd)
            throws ValidationException {
        final ValidationMessageContainer validationMessageContainer = validateWithoutCheckForDuplicates(dishToAdd);
        if ( existingMenu.stream()
                .anyMatch(singleMenuDish -> singleMenuDish.getName()
                        .equals(dishToAdd.getName())) ) {
            validationMessageContainer.addError(MessageCode.Menu.NAME_EXISTS, NAME);
        }
        return validationMessageContainer;
    }
    
    private ValidationMessageContainer validateWithoutCheckForDuplicates(AddDishDTO dishToAdd) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        
        final String description = dishToAdd.getDescription();
        if ( StringUtils.isBlank(description) ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.Menu.FIELD_INVALID, DESCRIPTION, description), DESCRIPTION);
        }
        
        final Double price = dishToAdd.getPrice();
        if ( price == null || price < 0.0 ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.Menu.FIELD_INVALID, PRICE, price), PRICE);
        }
        
        final String name = dishToAdd.getName();
        if ( StringUtils.isBlank(name) ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.Menu.FIELD_INVALID, NAME, name), NAME);
        }
        
        return validationMessageContainer;
    }
    
}
