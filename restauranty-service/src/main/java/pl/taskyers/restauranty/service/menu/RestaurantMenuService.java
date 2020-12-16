package pl.taskyers.restauranty.service.menu;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.menu.DishNotFoundException;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;

public interface RestaurantMenuService {
    
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
