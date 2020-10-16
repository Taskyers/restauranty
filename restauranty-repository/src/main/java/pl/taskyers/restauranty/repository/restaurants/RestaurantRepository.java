package pl.taskyers.restauranty.repository.restaurants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    Optional<Restaurant> findByName(String name);
    
    Optional<Restaurant> findByPhoneNumber(String phoneNumber);
    
}
