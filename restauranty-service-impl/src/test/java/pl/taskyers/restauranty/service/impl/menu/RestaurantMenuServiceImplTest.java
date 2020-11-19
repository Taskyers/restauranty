package pl.taskyers.restauranty.service.impl.menu;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.menu.MenuRepository;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.*;

public class RestaurantMenuServiceImplTest {
    
    private RestaurantService restaurantService;
    
    private MenuRepository menuRepository;
    
    private RestaurantMenuServiceImpl restaurantMenuService;
    
    @BeforeEach
    public void setUp() {
        restaurantService = mock(RestaurantService.class);
        menuRepository = mock(MenuRepository.class);
        restaurantMenuService = new RestaurantMenuServiceImpl(restaurantService, menuRepository);
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
        final Set<SingleMenuDish> result = restaurantMenuService.getDishesForRestaurantAndType(restaurantName, DishType.TEA);
        
        // then
        assertThat(result, iterableWithSize(2));
    }
    
    @Test
    public void testAddingInvalidDish() {
        // given
        final String restaurantName = "test";
        final AddDishDTO dishDTO = new AddDishDTO("", null, -23.23, DishType.BURGER);
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(new Restaurant());
        
        // when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> restaurantMenuService.addDishToMenu(restaurantName, dishDTO));
        
        // then
        assertThat(result.getMessages(), iterableWithSize(3));
    }
    
    @Test
    public void testAddingExistingDish() {
        // given
        final String restaurantName = "test";
        final String duplicatedName = "testDish";
        final AddDishDTO dishDTO = new AddDishDTO(duplicatedName, "tees", 23.23, DishType.BURGER);
        final Restaurant restaurant = Restaurant.builder()
                .menu(Sets.newHashSet(SingleMenuDish.builder()
                        .name(duplicatedName)
                        .build()))
                .build();
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(restaurant);
        
        // when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> restaurantMenuService.addDishToMenu(restaurantName, dishDTO));
        
        // then
        assertThat(result.getMessages(), iterableWithSize(1));
        assertThat(result.getMessages()
                .get(0)
                .getMessage(), is(MessageCode.Menu.NAME_EXISTS));
    }
    
    @Test
    public void testAddingValidDish() {
        // given
        final String restaurantName = "test";
        final Restaurant restaurant = new Restaurant();
        final AddDishDTO dishDTO = new AddDishDTO("asdf", "tees", 23.23, DishType.BURGER);
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(restaurant);
        when(menuRepository.save(any(SingleMenuDish.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final SingleMenuDish result = restaurantMenuService.addDishToMenu(restaurantName, dishDTO);
        
        // then
        assertThat(result, notNullValue());
    }
    
    @Test
    public void testEditingSameDish() {
        // given
        final Long id = 1L;
        final String restaurantName = "test";
        final SingleMenuDish singleMenuDish = new SingleMenuDish(id, "test", "test", 1.0, DishType.TEA);
        final AddDishDTO addDishDTO = new AddDishDTO("test", "test", 1.0, DishType.TEA);
        when(menuRepository.findById(id)).thenReturn(Optional.of(singleMenuDish));
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(new Restaurant());
        
        // when
        final SingleMenuDish result = restaurantMenuService.editDish(id, restaurantName, addDishDTO);
        
        // then
        assertThat(result, is(singleMenuDish));
    }
    
    @Test
    public void testEditingDifferentDish() {
        // given
        final Long id = 1L;
        final String restaurantName = "test";
        final SingleMenuDish singleMenuDish = new SingleMenuDish(id, "test", "test", 1.0, DishType.TEA);
        final AddDishDTO addDishDTO = new AddDishDTO("test123", "testasd", 1.0, DishType.SALAD);
        when(menuRepository.findById(id)).thenReturn(Optional.of(singleMenuDish));
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(new Restaurant());
        
        // when
        final SingleMenuDish result = restaurantMenuService.editDish(id, restaurantName, addDishDTO);
        
        // then
        assertThat(result.getId(), is(id));
        assertThat(result.getName(), is(addDishDTO.getName()));
        assertThat(result.getDescription(), is(addDishDTO.getDescription()));
        assertThat(result.getPrice(), is(addDishDTO.getPrice()));
        assertThat(result.getType(), is(addDishDTO.getType()));
    }
    
    @Test
    public void testRemovingExistingDish() {
        // given
        final Long id = 1L;
        final String restaurantName = "test";
        final SingleMenuDish singleMenuDish = new SingleMenuDish(id, "test", "test", 1.0, DishType.TEA);
        final Restaurant restaurant = Restaurant.builder()
                .menu(Sets.newHashSet(singleMenuDish))
                .build();
        when(menuRepository.findById(id)).thenReturn(Optional.of(singleMenuDish));
        when(restaurantService.getRestaurant(restaurantName)).thenReturn(restaurant);
        
        // when
        restaurantMenuService.removeDish(id, restaurantName);
        
        // then
        assertThat(restaurant.getMenu(), empty());
        verify(menuRepository).deleteById(id);
    }
    
}