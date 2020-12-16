package pl.taskyers.restauranty.service.openhour;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.openhour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.openhour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.Set;

public interface OpenHourService {
    
    Set<OpenHour> deleteAndSaveAll(@NonNull final Restaurant restaurant, @NonNull final Set<OpenHourDTO> openHours);
    
}
