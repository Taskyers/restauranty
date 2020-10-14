package pl.taskyers.restauranty.web.recovery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryDTO {
    
    private String token;
    
    private String password;
    
}
