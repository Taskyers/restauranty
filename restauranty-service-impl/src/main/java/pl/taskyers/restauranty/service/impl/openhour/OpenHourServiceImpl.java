package pl.taskyers.restauranty.service.impl.open_hour;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.utils.DateUtils;
import pl.taskyers.restauranty.repository.open_hour.OpenHourRepository;
import pl.taskyers.restauranty.service.open_hour.OpenHourService;

import java.time.DayOfWeek;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenHourServiceImpl implements OpenHourService {
    
    private final OpenHourRepository openHourRepository;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<OpenHour> deleteAndSaveAll(@NonNull Restaurant restaurant, @NonNull Set<OpenHourDTO> openHours) {
        final Set<OpenHour> result = Sets.newHashSetWithExpectedSize(openHours.size());
        openHourRepository.deleteAllByRestaurant(restaurant);
        for ( OpenHourDTO openHour : openHours ) {
            final OpenHour toSave = OpenHour.builder()
                    .dayOfWeek(DayOfWeek.valueOf(openHour.getDayOfWeek().toUpperCase()).getValue())
                    .openTime(DateUtils.parseTime(openHour.getOpenTime()))
                    .closeTime(DateUtils.parseTime(openHour.getCloseTime()))
                    .restaurant(restaurant)
                    .build();
            result.add(openHourRepository.save(toSave));
        }
        return result;
    }
    
}
