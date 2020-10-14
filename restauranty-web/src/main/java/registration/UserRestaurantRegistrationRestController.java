package registration;

import Registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;

import static Registration.RegistrationService.*;

@RestController
@RequestMapping(value = RESTAURANT_PREFIX)
@AllArgsConstructor
public class UserRestaurantRegistrationRestController {
    
    @Qualifier("userRestaurantService")
    private final RegistrationService registrationService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody AccountDTO accountDTO) {
        return registrationService.register(accountDTO);
    }
    
    @RequestMapping(value = FIND_BY_USERNAME, method = RequestMethod.GET)
    public boolean userExistsByUsername(@PathVariable String username) {
        return registrationService.accountExistsByUsername(username);
    }
    
    @RequestMapping(value = FIND_BY_EMAIL, method = RequestMethod.GET)
    public boolean userExistsByEmail(@PathVariable String email) {
        return registrationService.accountExistsByEmail(email);
    }
    
    
}
