package pl.taskyers.restauranty.service.recovery;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.tokens.TokenNotFoundException;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;

/**
 * Interface for password recovery
 *
 * @author Jakub Sildatk
 */
public interface PasswordRecoveryService {
    
    String RECOVERY_PREFIX = "/recovery";
    
    String GENERATE_TOKEN = "/generateToken";
    
    String SET_PASSWORD = "/setPassword";
    
    /**
     * Send email message with generated {@link pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken}
     *
     * @param email user's email address
     * @throws UserNotFoundException if user with provided email address was not found
     * @since 1.0.0
     */
    void sendEmailWithToken(@NonNull final String email) throws UserNotFoundException;
    
    /**
     * Set new password for user based on provided token. After password change, token will be removed from database
     *
     * @param token    {@link pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken} containing {@link pl.taskyers.restauranty.core.data.users.entity.UserBase}
     * @param password new password
     * @throws TokenNotFoundException if provided token was not found
     * @throws ValidationException    if provided password is not valid
     * @since 1.0.0
     */
    void setNewPassword(@NonNull final String token, @NonNull final String password) throws TokenNotFoundException, ValidationException;
    
}
