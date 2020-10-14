package pl.taskyers.restauranty.service.impl.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.users.RoleRepository;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.impl.PasswordEncoderHelper;
import pl.taskyers.restauranty.service.impl.registration.validator.RegistrationValidator;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.converters.AccountConverter;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.service.registration.RegistrationService;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    
    private final RegistrationValidator registrationValidator;
    
    private final PasswordEncoderHelper passwordEncoderHelper;
    
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
    
    @Override
    public UserBase register(@NonNull AccountDTO accountDTO) {
        final ValidationMessageContainer validationMessageContainer = registrationValidator.validate(accountDTO);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        final UserBase toSave = AccountConverter.convertFromDTO(accountDTO);
        toSave.setPassword(passwordEncoderHelper.getEncodedPassword(toSave.getPassword()));
        toSave.setRole(resolveRole(toSave));
        
        return userRepository.save(toSave);
    }
    
    @Override
    public boolean accountExistsByEmail(@NonNull String email) {
        return userRepository.findByEmail(email)
                .isPresent();
    }
    
    @Override
    public boolean accountExistsByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
                .isPresent();
    }
    
    private Role resolveRole(UserBase user) {
        return user instanceof UserClient ? roleRepository.findByRole(RoleType.ROLE_CLIENT) : roleRepository.findByRole(RoleType.ROLE_RESTAURANT);
    }
    
}
