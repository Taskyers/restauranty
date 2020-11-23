package pl.taskyers.restauranty.service.restaurants;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    
    String RESTAURANT_PREFIX = "/restaurant";
    
    String GET_RESTAURANT_BY_ID = "/{id}";
    
    String GET_RESTAURANT_BY_NAME = "/checkByName/{name}";
    
    String GET_RESTAURANT_BY_PHONE_NUMBER = "/checkByPhone/{phoneNumber}";
    
    Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO);
    
    Restaurant getRestaurant(@NonNull final Long id);
    
    Restaurant getRestaurant(@NonNull final String name);
    
    List<Restaurant> getUserRestaurants();
    
    Restaurant editRestaurant(@NonNull final Long id, @NonNull RestaurantDTO restaurantDTO);
    
    void deleteRestaurant(@NonNull final Long id);
    
    boolean restaurantExistsByName(@NonNull final String name);
    
    boolean restaurantExistsByPhoneNumber(@NonNull final String phoneNumber);
    
}
