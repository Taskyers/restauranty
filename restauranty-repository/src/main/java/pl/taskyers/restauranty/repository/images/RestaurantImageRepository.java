package pl.taskyers.restauranty.repository.images;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;

import java.util.Optional;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
    
    Optional<RestaurantImage> findByName(String name);
    
}
