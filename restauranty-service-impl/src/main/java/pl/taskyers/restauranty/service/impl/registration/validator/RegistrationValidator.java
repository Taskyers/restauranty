package pl.taskyers.restauranty.service.impl.registration.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.utils.ValidationUtils;
import pl.taskyers.restauranty.repository.users.UserRepository;

import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationValidator {
    
    private static final String FIELD_USERNAME = "username";
    
    private static final String FIELD_EMAIL = "email";
    
    private static final String FIELD_PASSWORD = "password";
    
    private final UserRepository userRepository;
    
    public ValidationMessageContainer validate(AccountDTO accountDTO) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        validateUsername(accountDTO.getUsername(), validationMessageContainer);
        validateEmail(accountDTO.getEmail(), validationMessageContainer);
        validatePassword(accountDTO.getPassword(), validationMessageContainer);
        return validationMessageContainer;
    }
    
    private void validateUsername(String username, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(username) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Username"), FIELD_USERNAME);
        } else if ( !ValidationUtils.isUserNameValid(username) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "username"), FIELD_USERNAME);
        } else if ( userRepository.findByUsername(username)
                .isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, username, "username");
            validationMessageContainer.addError(message, FIELD_USERNAME);
            log.debug(message);
        }
    }
    
    private void validateEmail(String email, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(email) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Email"), FIELD_EMAIL);
        } else if ( !ValidationUtils.isUserEmailValid(email) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "email"), FIELD_EMAIL);
        } else if ( userRepository.findByEmail(email)
                .isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, email, "email");
            validationMessageContainer.addError(message, FIELD_EMAIL);
            log.debug(message);
        }
    }
    
    private void validatePassword(String password, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(password) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Password"), FIELD_PASSWORD);
        } else if ( !ValidationUtils.isUserPasswordValid(password) ) {
            validationMessageContainer.addError(getMessage(PASSWORD_INVALID_FORMAT), FIELD_PASSWORD);
        }
    }
    
}
