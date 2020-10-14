package dao;

import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

import java.util.Optional;

public interface UserRestaurantDAO {
    
    UserRestaurant registerUser(UserRestaurant userRestaurantEntity);
    
    Optional<UserRestaurant> getEntityByEmail(String email);
    
    Optional<UserRestaurant> getEntityByUsername(String username);
    
    Optional<UserRestaurant> getEntityById(Long id);
}
