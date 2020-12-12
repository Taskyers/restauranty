package pl.taskyers.restauranty.service.impl.open_hour;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.repository.open_hour.OpenHourRepository;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.mockito.Mockito.*;

public class OpenHourServiceImplTest {
    
    private OpenHourRepository openHourRepository;
    
    private OpenHourServiceImpl openHourService;
    
    @BeforeEach
    public void setUp() {
        openHourRepository = mock(OpenHourRepository.class);
        openHourService = new OpenHourServiceImpl(openHourRepository);
    }
    
    @Test
    public void testDeletingAndSavingOpenHours() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        OpenHourDTO openHour = new OpenHourDTO("MONDAY", "12:00", "13:00");
        Set<OpenHourDTO> openHourDTOS = Sets.newHashSet(openHour);
        
        //when
        Set<OpenHour> result = openHourService.deleteAndSaveAll(restaurant, openHourDTOS);
        
        //then
        verify(openHourRepository).deleteAllByRestaurant(restaurant);
        verify(openHourRepository).save(any());
        assertThat(result, iterableWithSize(1));
    }
    
}
