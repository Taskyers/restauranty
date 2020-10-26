package pl.taskyers.restauranty.service.impl.reviews.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;

@UtilityClass
public class ClientReviewValidator {
    
    public ValidationMessageContainer validate(final String content) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        if ( StringUtils.isBlank(content) ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.FIELD_EMPTY, "content"), "content");
        }
        return validationMessageContainer;
    }
    
}
