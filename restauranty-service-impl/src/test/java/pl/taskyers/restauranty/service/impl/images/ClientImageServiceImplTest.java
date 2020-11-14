package pl.taskyers.restauranty.service.impl.images;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ClientImageServiceImplTest {
    
    private RestaurantRepository restaurantRepository;
    
    private RestaurantImageRepository restaurantImageRepository;
    
    private ClientImageServiceImpl clientImageService;
    
    @BeforeEach
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        restaurantImageRepository = mock(RestaurantImageRepository.class);
        clientImageService = new ClientImageServiceImpl(restaurantRepository, restaurantImageRepository);
    }
    
    @Test
    public void testGettingMainImageForNotExistingRestaurant() {
        // given
        final String name = "test";
        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> clientImageService.getMainImageForRestaurant(name));
        
        // then
        assertThat(result.getMessage(), is("Restaurant with name " + name + " was not found"));
    }
    
    @Test
    public void testGettingNotExistingMainImage() {
        // given
        final String name = "test";
        final Restaurant restaurant = Restaurant.builder()
                .name(name)
                .build();
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        when(restaurantImageRepository.findByRestaurantAndMain(restaurant, true)).thenReturn(Optional.empty());
        
        // when
        final ImageNotFoundException result = assertThrows(ImageNotFoundException.class, () -> clientImageService.getMainImageForRestaurant(name));
        
        // then
        assertThat(result.getMessage(), is("Restaurant " + name + " has no main image"));
    }
    
    @Test
    public void testGettingExistingMainImage() {
        // given
        final String name = "test";
        final Restaurant restaurant = Restaurant.builder()
                .name(name)
                .build();
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        when(restaurantImageRepository.findByRestaurantAndMain(restaurant, true)).thenReturn(Optional.of(new RestaurantImage()));
        
        // when
        clientImageService.getMainImageForRestaurant(name);
        
        // then
        verify(restaurantImageRepository).findByRestaurantAndMain(restaurant, true);
    }
    
}