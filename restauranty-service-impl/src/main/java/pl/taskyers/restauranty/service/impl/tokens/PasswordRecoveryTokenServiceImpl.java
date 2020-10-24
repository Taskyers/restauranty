package pl.taskyers.restauranty.service.impl.tokens;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.repository.tokens.PasswordRecoveryTokenRepository;
import pl.taskyers.restauranty.service.tokens.PasswordRecoveryTokenService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService {
    
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    @Override
    public PasswordRecoveryToken createToken(final UserBase user) {
        final PasswordRecoveryToken existingToken = passwordRecoveryTokenRepository.findByUser(user);
        if ( existingToken != null ) {
            log.debug("Updating existing token for user with id: {}", user.getId());
            existingToken.setToken(generateToken());
            return passwordRecoveryTokenRepository.save(existingToken);
        }
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken();
        passwordRecoveryToken.setUser(user);
        passwordRecoveryToken.setToken(generateToken());
        return passwordRecoveryTokenRepository.save(passwordRecoveryToken);
    }
    
    @Override
    public Optional<PasswordRecoveryToken> getTokenByString(@NonNull String token) {
        return passwordRecoveryTokenRepository.findByToken(token);
    }
    
    @Override
    public void deleteToken(final String token) {
        passwordRecoveryTokenRepository.findByToken(token)
                .ifPresent(passwordRecoveryToken -> passwordRecoveryTokenRepository.deleteById(passwordRecoveryToken.getId()));
    }
    
    private String generateToken() {
        String token = UUID.randomUUID()
                .toString();
        
        while ( passwordRecoveryTokenRepository.findByToken(token)
                .isPresent() ) {
            log.debug("Generated token already exists in database: {}. Generating another one.", token);
            token = UUID.randomUUID()
                    .toString();
        }
        return token;
    }
    
}
