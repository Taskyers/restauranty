package pl.taskyers.restauranty.service.impl.chat.validator;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;

@UtilityClass
public class ChatRoomValidator {
    
    public ValidationMessageContainer validate(final RoleType authorRole, final RoleType recipientRole) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        if ( authorRole == recipientRole ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.ChatMessage.WRONG_RECIPIENT), "recipient");
        }
        return validationMessageContainer;
    }
    
}
