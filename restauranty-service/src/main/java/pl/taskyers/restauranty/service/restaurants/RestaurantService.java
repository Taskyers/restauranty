package pl.taskyers.restauranty.service.restaurants;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

public interface RestaurantService {
    
    String RESTAURANT_PREFIX = "/restaurant";
    
    Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO);
    
}
