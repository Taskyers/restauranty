package pl.taskyers.restauranty.service.open_hour;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.Set;

public interface OpenHourService {
    
    Set<OpenHour> deleteAndSaveAll(@NonNull final Restaurant restaurant, @NonNull final Set<OpenHourDTO> openHours);
    
}
