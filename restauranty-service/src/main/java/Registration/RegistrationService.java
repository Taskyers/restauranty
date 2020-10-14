package Registration;

import org.springframework.http.ResponseEntity;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;

public interface RegistrationService {
    
    String REGISTRATION_PREFIX = "/register";
    
    String CLIENT_PREFIX = REGISTRATION_PREFIX + "/client";
    
    String RESTAURANT_PREFIX = REGISTRATION_PREFIX + "/restaurant";
    
    String FIND_BY_EMAIL = "/checkByEmail/{email}";
    
    String FIND_BY_USERNAME = "/checkByUsername/{username}";
    
    ResponseEntity register(AccountDTO accountDTO);
    
    boolean accountExistsByEmail(String email);
    
    boolean accountExistsByUsername(String username);
    
}
