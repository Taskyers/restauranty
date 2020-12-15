package pl.taskyers.restauranty.repository.openhour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.openhour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, Long> {
    
    void deleteAllByRestaurant(Restaurant restaurant);
    
}
