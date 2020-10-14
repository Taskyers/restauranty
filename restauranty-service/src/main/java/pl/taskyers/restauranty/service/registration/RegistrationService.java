package pl.taskyers.restauranty.service.registration;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;

public interface RegistrationService {
    
    String REGISTRATION_PREFIX = "/register";
    
    String FIND_BY_EMAIL = "/checkByEmail/{email}";
    
    String FIND_BY_USERNAME = "/checkByUsername/{username}";
    
    UserBase register(@NonNull AccountDTO accountDTO) throws ValidationException;
    
    boolean accountExistsByEmail(@NonNull String email);
    
    boolean accountExistsByUsername(@NonNull String username);
    
}
