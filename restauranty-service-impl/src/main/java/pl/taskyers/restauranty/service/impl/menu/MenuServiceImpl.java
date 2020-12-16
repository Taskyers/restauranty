package pl.taskyers.restauranty.service.impl.menu;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.service.menu.MenuService;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    
    private final RestaurantService restaurantService;
    
    @Override
    public Set<SingleMenuDish> getMenuForRestaurant(@NonNull String restaurant) throws RestaurantNotFoundException {
        return restaurantService.getRestaurant(restaurant)
                .getMenu();
    }
    
    @Override
    public Set<SingleMenuDish> getDishesForRestaurantAndType(@NonNull String restaurant, @NonNull DishType dishType)
            throws RestaurantNotFoundException {
        final Restaurant restaurantFromDatabase = restaurantService.getRestaurant(restaurant);
        return restaurantFromDatabase.getMenu()
                .stream()
                .filter(singleMenuDish -> singleMenuDish.getType() == dishType)
                .collect(Collectors.toSet());
    }
    
}
