package pl.taskyers.restauranty.service.impl.chat.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;

@UtilityClass
public class ChatMessageValidator {
    
    public ValidationMessageContainer validate(final RoleType authorRole, final RoleType recipientRole, final String content) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        if ( StringUtils.isBlank(content) ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.FIELD_EMPTY, "content"), "content");
        }
        if ( authorRole == recipientRole ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.ChatMessage.WRONG_RECIPIENT), "recipient");
        }
        return validationMessageContainer;
    }
    
}
