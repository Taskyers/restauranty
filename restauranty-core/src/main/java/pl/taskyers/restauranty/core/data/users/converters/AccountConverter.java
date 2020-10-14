package pl.taskyers.restauranty.core.data.users.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;

@UtilityClass
public class AccountConverter {
    
    public UserBase convertFromDTO(AccountDTO accountDTO) {
        UserBase userBase = accountDTO.getRole() == RoleType.ROLE_RESTAURANT ? new UserRestaurant() : new UserClient();
        userBase.setUsername(accountDTO.getUsername());
        userBase.setPassword(accountDTO.getPassword());
        userBase.setEmail(accountDTO.getEmail());
        return userBase;
    }
    
}
