package dao;

import pl.taskyers.restauranty.core.data.users.entity.UserClient;

import java.util.Optional;

public interface UserClientDAO {
    
    UserClient registerUser(UserClient userClientEntity);
    
    Optional<UserClient> getEntityByEmail(String email);
    
    Optional<UserClient> getEntityByUsername(String username);
    
    Optional<UserClient> getEntityById(Long id);
    
}
