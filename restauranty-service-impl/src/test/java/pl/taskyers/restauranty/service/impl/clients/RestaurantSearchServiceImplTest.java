package pl.taskyers.restauranty.service.impl.clients;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RestaurantSearchServiceImplTest {
    
    private RestaurantRepository restaurantRepository;
    
    private RestaurantSearchServiceImpl restaurantSearchService;
    
    @BeforeEach
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        restaurantSearchService = new RestaurantSearchServiceImpl(restaurantRepository);
    }
    
    @Test
    public void testSearchingWithoutRestaurantAndTags() {
        // given
        final String restaurantName = "";
        final Set<String> tags = new HashSet<>();
        
        // when
        restaurantSearchService.searchForRestaurants(restaurantName, tags);
        
        // then
        verify(restaurantRepository).findAll();
    }
    
    @Test
    public void testSearchingWithoutTags() {
        // given
        final String restaurantName = "test";
        final Set<String> tags = new HashSet<>();
        
        // when
        restaurantSearchService.searchForRestaurants(restaurantName, tags);
        
        // then
        verify(restaurantRepository).findAllByNameContainingIgnoreCase(restaurantName);
    }
    
    @Test
    public void testSearchingWithoutRestaurantName() {
        // given
        final String restaurantName = "";
        final Set<String> tags = Sets.newHashSet("tag");
        
        // when
        restaurantSearchService.searchForRestaurants(restaurantName, tags);
        
        // then
        verify(restaurantRepository).findAllByTagsValueIsIn(tags);
    }
    
    @Test
    public void testSearchingWithRestaurantNameAndTags() {
        // given
        final String restaurantName = "test";
        final Set<String> tags = Sets.newHashSet("tag");
        
        // when
        restaurantSearchService.searchForRestaurants(restaurantName, tags);
        
        // then
        verify(restaurantRepository).findAllByNameContainingIgnoreCaseAndTagsValueIsIn(restaurantName, tags);
    }
    
}