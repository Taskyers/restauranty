package pl.taskyers.restauranty.web.users.dto;

import lombok.Value;

@Value
public class UserDTO {
    
    long id;
    
    String username;
    
    String email;
    
    String accountType;
    
    boolean enabled;
    
}
