package pl.taskyers.restauranty.service.restaurants;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    
    String RESTAURANT_PREFIX = "/restaurant";
    
    String GET_RESTAURANT_BY_ID = "/{id}";
    
    Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO);
    
    Restaurant getRestaurant(@NonNull final Long id);
    
    List<Restaurant> getUserRestaurants();
    
    Restaurant editRestaurant(@NonNull final Long id, @NonNull RestaurantDTO restaurantDTO);
    
    void deleteRestaurant(@NonNull final Long id);
    
}
