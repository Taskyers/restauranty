package pl.taskyers.restauranty.service.users;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.Collection;

public interface AdminUsersService {
    
    String PREFIX = "/admin/users";
    
    String BAN_USER = "/ban";
    
    String UNBAN_USER = "/unban";
    
    String BY_ID = "/{id}";
    
    Collection<UserBase> getUsers();
    
    UserBase banUser(@NonNull final Long id) throws UserNotFoundException;
    
    UserBase unbanUser(@NonNull final Long id) throws UserNotFoundException;
    
}
