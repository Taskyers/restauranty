package pl.taskyers.restauranty.core.data.users.converters;

import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

public class AccountConverter {
    
    private AccountConverter() {
    }
    
    public static UserClient convertFromDTOToClient(AccountDTO accountDTO) {
        UserClient userClient = new UserClient();
        userClient.setUsername(accountDTO.getUsername());
        userClient.setEmail(accountDTO.getEmail());
        userClient.setPassword(accountDTO.getPassword());
        userClient.setRole(accountDTO.getRole());
        return userClient;
    }
    
    public static UserRestaurant convertFromDTOToRestaurant(AccountDTO accountDTO) {
        UserRestaurant userRestaurant = new UserRestaurant();
        userRestaurant.setUsername(accountDTO.getUsername());
        userRestaurant.setEmail(accountDTO.getEmail());
        userRestaurant.setPassword(accountDTO.getPassword());
        userRestaurant.setRole(accountDTO.getRole());
        return userRestaurant;
    }
    
    public static AccountDTO convertToDTO(UserBase userBase) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername(userBase.getUsername());
        accountDTO.setEmail(userBase.getEmail());
        accountDTO.setPassword(userBase.getPassword());
        accountDTO.setRole(userBase.getRole());
        return accountDTO;
    }
    
}
