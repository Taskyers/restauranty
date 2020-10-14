package pl.taskyers.restauranty.core.data.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    
    private RoleType role;
    
    private String username;
    
    private String password;
    
    private String email;
    
}
