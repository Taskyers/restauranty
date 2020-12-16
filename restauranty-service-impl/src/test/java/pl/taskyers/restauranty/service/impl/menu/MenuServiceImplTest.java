package pl.taskyers.restauranty.service.impl.menu;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.mockito.Mockito.*;

public class MenuServiceImplTest {
    
    private RestaurantService restaurantService;
    
    private MenuServiceImpl menuService;
    
    @BeforeEach
    public void setUp() {
        restaurantService = mock(RestaurantService.class);
        menuService = new MenuServiceImpl(restaurantService);
    }
    
    @Test
    public void testFilteringMenu() {
        // given
        final String restaurantName = "test";
        final SingleMenuDish dish1 = SingleMenuDish.builder()
                .name("dish1")
                .type(DishType.TEA)
                .build();
        final SingleMenuDish dish2 = SingleMenuDish.builder()
                .name("dish2")
                .type(DishType.TEA)
                .build();
        final SingleMenuDish dish3 = SingleMenuDish.builder()
                .name("dish3")
                .type(DishType.BURGER)
                .build();
        final Restaurant restaurant = Restaurant.builder()
                .menu(Sets.newHashSet(dish1, dish2, dish3))
                .build();
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(restaurant);
        
        // when
        final Set<SingleMenuDish> result = menuService.getDishesForRestaurantAndType(restaurantName, DishType.TEA);
        
        // then
        assertThat(result, iterableWithSize(2));
    }
    
}