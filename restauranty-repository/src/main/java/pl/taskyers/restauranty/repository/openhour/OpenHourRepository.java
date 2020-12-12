package pl.taskyers.restauranty.repository.open_hour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.Set;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, Long> {
    
    Set<OpenHour> findAllByRestaurant(Restaurant restaurant);
    
    void deleteAllByRestaurant(Restaurant restaurant);
}
