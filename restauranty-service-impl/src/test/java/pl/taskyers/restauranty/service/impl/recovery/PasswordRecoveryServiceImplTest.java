package pl.taskyers.restauranty.service.impl.recovery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.tokens.TokenNotFoundException;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.email.service.EmailService;
import pl.taskyers.restauranty.email.service.enums.EmailType;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.impl.PasswordEncoderHelper;
import pl.taskyers.restauranty.service.tokens.PasswordRecoveryTokenService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PasswordRecoveryServiceImplTest {
    
    private PasswordRecoveryServiceImpl passwordRecoveryService;
    
    private EmailService emailService;
    
    private PasswordRecoveryTokenService passwordRecoveryTokenService;
    
    private UserRepository userRepository;
    
    private PasswordEncoderHelper passwordEncoderHelper;
    
    @BeforeEach
    public void setUp() {
        emailService = mock(EmailService.class);
        passwordRecoveryTokenService = mock(PasswordRecoveryTokenService.class);
        userRepository = mock(UserRepository.class);
        passwordEncoderHelper = mock(PasswordEncoderHelper.class);
        passwordRecoveryService = new PasswordRecoveryServiceImpl(userRepository, passwordRecoveryTokenService, emailService, passwordEncoderHelper);
    }
    
    @Test
    public void testSendingEmailWhenUserIsNotFound() {
        // given
        final String email = "test@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        // when
        final UserNotFoundException result = assertThrows(UserNotFoundException.class, () -> passwordRecoveryService.sendEmailWithToken(email));
        
        // then
        assertThat(result.getMessage(), is("Email was not found"));
    }
    
    @Test
    public void testSendingEmailWhenUserIsFound() {
        // given
        final String email = "test@email.com";
        final UserBase userBase = new UserClient();
        userBase.setEmail(email);
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userBase));
        when(passwordRecoveryTokenService.createToken(userBase)).thenReturn(passwordRecoveryToken);
        
        // when
        passwordRecoveryService.sendEmailWithToken(email);
        
        // then
        verify(passwordRecoveryTokenService).createToken(userBase);
        verify(emailService).sendEmailToSingleAddressee(email, EmailType.PASSWORD_RECOVERY, passwordRecoveryToken.getToken());
    }
    
    @Test
    public void testSettingNewPasswordWhenTokenIsNotFound() {
        // given
        final String token = "test";
        when(passwordRecoveryTokenService.getTokenByString(token)).thenReturn(Optional.empty());
        
        // when
        final TokenNotFoundException result = assertThrows(TokenNotFoundException.class, () -> passwordRecoveryService.setNewPassword(token, "test"));
        
        // then
        assertThat(result.getMessage(), is("Token was not found"));
    }
    
    @Test
    public void testSettingNewPasswordWhenTokenIsFound() {
        // given
        final String token = "test";
        final String password = "zaq1@WSX";
        final UserBase userBase = new UserClient();
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken();
        passwordRecoveryToken.setToken(token);
        passwordRecoveryToken.setUser(userBase);
        when(passwordRecoveryTokenService.getTokenByString(token)).thenReturn(Optional.of(passwordRecoveryToken));
        
        // when
        passwordRecoveryService.setNewPassword(token, password);
        
        // then
        verify(passwordEncoderHelper).getEncodedPassword(password);
        verify(userRepository).save(userBase);
        verify(passwordRecoveryTokenService).deleteToken(token);
    }
    
    @Test
    public void testSettingNewPasswordWithNotValidPassword() {
        // given
        final String token = "test";
        final String password = "test";
        final UserBase userBase = new UserClient();
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken();
        passwordRecoveryToken.setToken(token);
        passwordRecoveryToken.setUser(userBase);
        when(passwordRecoveryTokenService.getTokenByString(token)).thenReturn(Optional.of(passwordRecoveryToken));
        
        // when
        final ValidationException result = assertThrows(ValidationException.class, () -> passwordRecoveryService.setNewPassword(token, password));
        
        // then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
}