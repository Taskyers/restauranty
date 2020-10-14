package Registration.dao;

import dao.UserRestaurantDAO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.repository.users.RestaurantUserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRestaurantDAOImpl implements UserRestaurantDAO {
    
    private final RestaurantUserRepository restaurantUserRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    
    @Override
    public UserRestaurant registerUser(UserRestaurant userRestaurantEntity) {
        userRestaurantEntity.setPassword(passwordEncoder.encode(userRestaurantEntity.getPassword()));
        return restaurantUserRepository.save(userRestaurantEntity);
    }
    
    @Override
    public Optional<UserRestaurant> getEntityByEmail(String email) {
        return restaurantUserRepository.findByEmail(email);
    }
    
    @Override
    public Optional<UserRestaurant> getEntityByUsername(String username) {
        return restaurantUserRepository.findByUsername(username);
    }
    
    @Override
    public Optional<UserRestaurant> getEntityById(Long id) {
        return restaurantUserRepository.findById(id);
    }
    
}
