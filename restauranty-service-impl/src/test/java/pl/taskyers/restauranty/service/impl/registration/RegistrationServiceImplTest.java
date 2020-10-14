package pl.taskyers.restauranty.service.impl.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.users.RoleRepository;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.impl.PasswordEncoderHelper;
import pl.taskyers.restauranty.service.impl.registration.validator.RegistrationValidator;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegistrationServiceImplTest {
    
    private static final String USERNAME = "Test";
    
    private static final String INVALID_EMAIL = "test";
    
    private static final String VALID_EMAIL = "test@test.com";
    
    private static final String INVALID_PASSWORD = "abc";
    
    private static final String VALID_PASSWORD = "Password12#";
    
    private RegistrationServiceImpl registrationService;
    
    private RegistrationValidator registrationValidator;
    
    private PasswordEncoderHelper passwordEncoderHelper;
    
    private UserRepository userRepository;
    
    private RoleRepository roleRepository;
    
    @BeforeEach
    public void setUp() {
        passwordEncoderHelper = mock(PasswordEncoderHelper.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        registrationValidator = new RegistrationValidator(userRepository);
        registrationService = new RegistrationServiceImpl(registrationValidator, passwordEncoderHelper, userRepository, roleRepository);
    }
    
    @Test
    public void testRegisterWithBlankFields() {
        //given
        AccountDTO accountDTO = new AccountDTO(RoleType.ROLE_CLIENT, "", "", "");
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> registrationService.register(accountDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(3));
    }
    
    @Test
    public void testRegisterWithExistingUserAndExistingValidEmailAndInvalidPasswordFields() {
        //given
        UserBase userBase = new UserClient();
        AccountDTO accountDTO = new AccountDTO(RoleType.ROLE_CLIENT, USERNAME, INVALID_PASSWORD, VALID_EMAIL);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(userBase));
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(userBase));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> registrationService.register(accountDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(3));
    }
    
    @Test
    public void testRegisterWithInvalidEmailField() {
        //given
        AccountDTO accountDTO = new AccountDTO(RoleType.ROLE_CLIENT, USERNAME, VALID_PASSWORD, INVALID_EMAIL);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> registrationService.register(accountDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testRegisterWithValidUserClient() {
        //given
        RoleType roleType = RoleType.ROLE_CLIENT;
        AccountDTO accountDTO = new AccountDTO(roleType, USERNAME, VALID_PASSWORD, VALID_EMAIL);
        
        //when
        registrationService.register(accountDTO);
        
        //then
        verify(passwordEncoderHelper).getEncodedPassword(VALID_PASSWORD);
        verify(roleRepository).findByRole(roleType);
        verify(userRepository).save(any(UserClient.class));
    }
    
    @Test
    public void testRegisterWithValidUserRestaurant() {
        //given
        RoleType roleType = RoleType.ROLE_RESTAURANT;
        AccountDTO accountDTO = new AccountDTO(roleType, USERNAME, VALID_PASSWORD, VALID_EMAIL);
        
        //when
        registrationService.register(accountDTO);
        
        //then
        verify(passwordEncoderHelper).getEncodedPassword(VALID_PASSWORD);
        verify(roleRepository).findByRole(roleType);
        verify(userRepository).save(any(UserRestaurant.class));
    }
    
}
