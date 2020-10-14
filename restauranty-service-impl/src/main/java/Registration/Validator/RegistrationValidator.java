package Registration.Validator;

import dao.UserClientDAO;
import dao.UserRestaurantDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.data.users.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.data.users.utils.ValidationUtils;

import static pl.taskyers.restauranty.core.data.users.messages.MessageCode.*;
import static pl.taskyers.restauranty.core.data.users.messages.MessageProvider.getMessage;

@Component("registrationValidator")
@AllArgsConstructor
@Slf4j
public class RegistrationValidator {
    
    private static final String FIELD_USERNAME = "username";
    
    private static final String FIELD_EMAIL = "email";
    
    private static final String FIELD_PASSWORD = "password";
    
    private final UserClientDAO userClientDAO;
    
    private final UserRestaurantDAO userRestaurantDAO;
    
    public void validate(AccountDTO accountDTO, ValidationMessageContainer validationMessageContainer) {
        validateUsername(accountDTO.getUsername(), accountDTO.getRole(), validationMessageContainer);
        validateEmail(accountDTO.getEmail(), accountDTO.getRole(), validationMessageContainer);
        validatePassword(accountDTO.getPassword(), validationMessageContainer);
    }
    
    private void validateUsername(String username, Role role, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(username) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Username"), FIELD_USERNAME);
        } else if ( role.getRole() == RoleType.ROLE_CLIENT && userClientDAO.getEntityByUsername(username).isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, username, "username");
            validationMessageContainer.addError(message, FIELD_USERNAME);
            log.warn(message);
        } else if ( role.getRole() == RoleType.ROLE_RESTAURANT && userRestaurantDAO.getEntityByUsername(username).isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, username, "username");
            validationMessageContainer.addError(message, FIELD_USERNAME);
            log.warn(message);
        }
    }
    
    private void validateEmail(String email, Role role, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(email) ) {
            validationMessageContainer.addError(getMessage(FIELD_EMPTY, "Email"), FIELD_EMAIL);
        } else if ( !ValidationUtils.isUserEmailValid(email) ) {
            validationMessageContainer.addError(getMessage(FIELD_INVALID_FORMAT, "email"), FIELD_EMAIL);
        } else if ( role.getRole() == RoleType.ROLE_CLIENT && userClientDAO.getEntityByEmail(email).isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, email, "email");
            validationMessageContainer.addError(message, FIELD_EMAIL);
            log.warn(message);
        } else if ( role.getRole() == RoleType.ROLE_RESTAURANT && userRestaurantDAO.getEntityByEmail(email).isPresent() ) {
            String message = getMessage(ACCOUNT_WITH_FIELD_EXISTS, email, "email");
            validationMessageContainer.addError(message, FIELD_EMAIL);
            log.warn(message);
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
