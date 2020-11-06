package pl.taskyers.restauranty.repository.restaurants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    Set<Restaurant> findAllByNameContainingIgnoreCase(String name);
    
    Set<Restaurant> findAllByTagsValueIsIn(Collection<String> values);
    
    Set<Restaurant> findAllByNameContainingIgnoreCaseAndTagsValueIsIn(String name, Collection<String> values);
    
    Optional<Restaurant> findByName(String name);
    
    Optional<Restaurant> findByPhoneNumber(String phoneNumber);
    
    List<Restaurant> findAllByOwner(UserBase userRestaurant);
    
}
