package pl.taskyers.restauranty.web.registration.dto;

import lombok.Value;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;

@Value
public class RegistrationResponseDTO {
    
    String username;
    
    String email;
    
    RoleType role;
    
}
