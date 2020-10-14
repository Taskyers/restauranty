package pl.taskyers.restauranty.service.tokens;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.Optional;

/**
 * Interface for password recovery token operations
 *
 * @author Jakub Sildatk
 */
public interface PasswordRecoveryTokenService {
    
    /**
     * Create {@link PasswordRecoveryToken} for provided user. Method checks for duplicates before saving token to database
     *
     * @param user {@link UserBase}
     * @return saved {@link PasswordRecoveryToken}
     * @since 1.0.0
     */
    PasswordRecoveryToken createToken(@NonNull UserBase user);
    
    /**
     * Get {@link PasswordRecoveryToken} by token string
     *
     * @param token token as string
     * @return {@link PasswordRecoveryToken} as optional
     * @since 1.0.0
     */
    Optional<PasswordRecoveryToken> getTokenByString(@NonNull String token);
    
    /**
     * Delete {@link PasswordRecoveryToken} by token string
     *
     * @param token token as string
     * @since 1.0.0
     */
    void deleteToken(@NonNull String token);
    
}
