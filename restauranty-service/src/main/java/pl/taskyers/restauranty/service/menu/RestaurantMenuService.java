package pl.taskyers.restauranty.service.menu;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.menu.DishNotFoundException;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;

import java.util.Set;

public interface RestaurantMenuService {
    
    String PREFIX = "/restaurant/menu";
    
    String BY_ID = "/{id}";
    
    String BY_RESTAURANT = "/{restaurant}";
    
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
    
    /**
     * Add single dish to restaurant's menu
     *
     * @param restaurant restaurant's name
     * @param addDishDTO {@link AddDishDTO}
     * @return saved {@link SingleMenuDish}
     * @throws RestaurantNotFoundException if restaurant was not found
     * @throws ValidationException         if validation did not pass - {@link pl.taskyers.restauranty.service.impl.menu.validator.RestaurantMenuValidator}
     * @since 1.0.0
     */
    SingleMenuDish addDishToMenu(@NonNull final String restaurant, @NonNull final AddDishDTO addDishDTO)
            throws RestaurantNotFoundException, ValidationException;
    
    /**
     * Edit single dish
     *
     * @param id         id of dish that will be edited
     * @param restaurant restaurant's name
     * @param addDishDTO {@link AddDishDTO}
     * @return edited {@link SingleMenuDish}
     * @throws DishNotFoundException       if dish was not found
     * @throws RestaurantNotFoundException if restaurant was not found
     * @throws ValidationException         if validation did not pass - {@link pl.taskyers.restauranty.service.impl.menu.validator.RestaurantMenuValidator}
     * @since 1.0.0
     */
    SingleMenuDish editDish(@NonNull final Long id, @NonNull final String restaurant, @NonNull final AddDishDTO addDishDTO)
            throws DishNotFoundException, RestaurantNotFoundException, ValidationException;
    
    /**
     * Remove single dish from database and restaurant's menu
     *
     * @param id         id of dish that will be removed
     * @param restaurant restaurant's name
     * @throws RestaurantNotFoundException if restaurant was not found
     * @throws DishNotFoundException       if dish was not found
     * @since 1.0.0
     */
    void removeDish(@NonNull final Long id, @NonNull final String restaurant) throws RestaurantNotFoundException, DishNotFoundException;
    
}
