package pl.taskyers.restauranty.service.impl.tokens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.repository.tokens.PasswordRecoveryTokenRepository;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class PasswordRecoveryTokenServiceImplTest {
    
    private PasswordRecoveryTokenServiceImpl passwordRecoveryTokenService;
    
    private PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    @BeforeEach
    public void setUp() {
        passwordRecoveryTokenRepository = mock(PasswordRecoveryTokenRepository.class);
        passwordRecoveryTokenService = new PasswordRecoveryTokenServiceImpl(passwordRecoveryTokenRepository);
    }
    
    @Test
    public void testCreatingToken() {
        // given
        final UserBase user = new UserClient();
        when(passwordRecoveryTokenRepository.save(any(PasswordRecoveryToken.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final PasswordRecoveryToken result = passwordRecoveryTokenService.createToken(user);
        
        // then
        assertThat(result, notNullValue());
        assertThat(result.getUser(), is(user));
        assertThat(result.getToken(), isA(String.class));
        verify(passwordRecoveryTokenRepository).findByToken(anyString());
    }
    
    @Test
    public void testUpdatingToken() {
        // given
        final UserBase user = new UserClient();
        final PasswordRecoveryToken token = new PasswordRecoveryToken();
        token.setUser(user);
        when(passwordRecoveryTokenRepository.save(any(PasswordRecoveryToken.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(passwordRecoveryTokenRepository.findByUser(user)).thenReturn(token);
        
        // when
        final PasswordRecoveryToken result = passwordRecoveryTokenService.createToken(user);
        
        // then
        assertThat(result, notNullValue());
        assertThat(result.getUser(), is(user));
        assertThat(result.getToken(), isA(String.class));
    }
    
    @Test
    public void testCreatingTokenWithDuplicate() {
        // given
        final UserBase user = new UserClient();
        when(passwordRecoveryTokenRepository.save(any(PasswordRecoveryToken.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(passwordRecoveryTokenRepository.findByToken(anyString())).thenReturn(Optional.of(new PasswordRecoveryToken()))
                .thenReturn(Optional.empty());
        
        // when
        final PasswordRecoveryToken result = passwordRecoveryTokenService.createToken(user);
        
        // then
        assertThat(result, notNullValue());
        assertThat(result.getUser(), is(user));
        assertThat(result.getToken(), isA(String.class));
        verify(passwordRecoveryTokenRepository, times(2)).findByToken(anyString());
    }
    
    @Test
    public void testDeletingExistingToken() {
        // given
        final String token = "test";
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken(1L, new UserClient(), token);
        when(passwordRecoveryTokenRepository.findByToken(token)).thenReturn(Optional.of(passwordRecoveryToken));
        
        // when
        passwordRecoveryTokenService.deleteToken(token);
        
        // then
        verify(passwordRecoveryTokenRepository).deleteById(passwordRecoveryToken.getId());
    }
    
    @Test
    public void testDeletingNotExistingToken() {
        // given
        final String token = "test";
        final PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken(1L, new UserClient(), token);
        when(passwordRecoveryTokenRepository.findByToken(token)).thenReturn(Optional.empty());
        
        // when
        passwordRecoveryTokenService.deleteToken(token);
        
        // then
        verify(passwordRecoveryTokenRepository, never()).deleteById(passwordRecoveryToken.getId());
    }
    
}