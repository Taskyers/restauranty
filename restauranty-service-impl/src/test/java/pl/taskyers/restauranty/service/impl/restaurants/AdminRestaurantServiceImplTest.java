package pl.taskyers.restauranty.service.impl.restaurants;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.repository.users.UserRestaurantRepository;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdminRestaurantServiceImplTest {
    
    private UserRestaurantRepository userRestaurantRepository;
    
    private AdminRestaurantServiceImpl adminRestaurantService;
    
    @BeforeEach
    public void setUp() {
        userRestaurantRepository = mock(UserRestaurantRepository.class);
        adminRestaurantService = new AdminRestaurantServiceImpl(userRestaurantRepository);
    }
    
    @Test
    public void testGettingNotVerifiedRestaurants() {
        // given
        final UserRestaurant userRestaurant = new UserRestaurant();
        userRestaurant.setId(1L);
        userRestaurant.setVerified(false);
        final UserRestaurant userRestaurant1 = new UserRestaurant();
        userRestaurant1.setId(2L);
        userRestaurant1.setVerified(false);
        final UserRestaurant userRestaurant2 = new UserRestaurant();
        userRestaurant2.setId(3L);
        userRestaurant2.setVerified(true);
        when(userRestaurantRepository.findAll()).thenReturn(Lists.newArrayList(userRestaurant, userRestaurant1, userRestaurant2));
        
        // when
        final Collection<UserRestaurant> result = adminRestaurantService.getNotVerifiedRestaurants();
        
        // then
        assertThat(result, allOf(iterableWithSize(2), hasItems(userRestaurant, userRestaurant1)));
    }
    
    @Test
    public void testVerifyingNotExistingRestaurant() {
        // given
        when(userRestaurantRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // when
        final UserNotFoundException result = assertThrows(UserNotFoundException.class, () -> adminRestaurantService.verify(2L));
        
        // then
        assertThat(result.getMessage(), is("User with id: 2 was not found"));
    }
    
    @Test
    public void testVerifyingExistingRestaurant() {
        // given
        final UserRestaurant userRestaurant = new UserRestaurant();
        userRestaurant.setVerified(false);
        when(userRestaurantRepository.findById(anyLong())).thenReturn(Optional.of(userRestaurant));
        when(userRestaurantRepository.save(userRestaurant)).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final UserRestaurant result = adminRestaurantService.verify(1L);
        
        // then
        assertThat(result.isVerified(), is(true));
    }
    
}