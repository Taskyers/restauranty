package pl.taskyers.restauranty.service.impl.recovery;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.tokens.TokenNotFoundException;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.ValidationMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.core.utils.ValidationUtils;
import pl.taskyers.restauranty.email.service.EmailService;
import pl.taskyers.restauranty.email.service.enums.EmailType;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.impl.PasswordEncoderHelper;
import pl.taskyers.restauranty.service.recovery.PasswordRecoveryService;
import pl.taskyers.restauranty.service.tokens.PasswordRecoveryTokenService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    
    private final UserRepository userRepository;
    
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    
    private final EmailService emailService;
    
    private final PasswordEncoderHelper passwordEncoderHelper;
    
    @Override
    public void sendEmailWithToken(@NonNull final String email) {
        final UserBase user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MessageProvider.getMessage(MessageCode.NOT_FOUND, "Email")));
        final PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenService.createToken(user);
        emailService.sendEmailToSingleAddressee(email, EmailType.PASSWORD_RECOVERY, passwordRecoveryToken.getToken());
    }
    
    @Override
    public void setNewPassword(@NonNull final String token, @NonNull final String password) {
        final PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenService.getTokenByString(token)
                .orElseThrow(() -> new TokenNotFoundException(MessageProvider.getMessage(MessageCode.NOT_FOUND, "Token")));
        final UserBase user = passwordRecoveryToken.getUser();
        validatePassword(password);
        user.setPassword(passwordEncoderHelper.getEncodedPassword(password));
        userRepository.save(user);
        passwordRecoveryTokenService.deleteToken(token);
    }
    
    private void validatePassword(String password) {
        if ( !ValidationUtils.isUserPasswordValid(password) ) {
            throw new ValidationException(Collections.singletonList(
                    new ValidationMessage(MessageProvider.getMessage(MessageCode.PASSWORD_INVALID_FORMAT), MessageType.ERROR, "password")));
        }
    }
    
}
