package pl.taskyers.restauranty.service.menu;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;;

import java.util.Set;

public interface MenuService {
    
    String RESTAURANT_PREFIX = "/restaurant/menu";
    
    String CLIENT_PREFIX = "/client/menu";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    String BY_ID = "/{id}";
    
    String BY_TYPE = "/{type}";
    
    /**
     * Get entire menu for given restaurant
     *
     * @param restaurant restaurant's name
     * @return {@link Set} of {@link SingleMenuDish}
     * @throws RestaurantNotFoundException if restaurant was not found
     * @since 1.0.0
     */
    Set<SingleMenuDish> getMenuForRestaurant(@NonNull final String restaurant) throws RestaurantNotFoundException;
    
    /**
     * Filter all dishes from restaurant by {@link DishType}
     *
     * @param restaurant restaurant's name
     * @param dishType   {@link DishType}
     * @return {@link Set} of filtered {@link SingleMenuDish}
     * @throws RestaurantNotFoundException if restaurant was not found
     * @since 1.0.0
     */
    Set<SingleMenuDish> getDishesForRestaurantAndType(@NonNull final String restaurant, @NonNull final DishType dishType)
            throws RestaurantNotFoundException;
    
}
