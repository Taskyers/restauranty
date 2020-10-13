package pl.taskyers.restauranty.repository.users;

import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

@Repository
public interface RestaurantUserRepository extends UserBaseRepository<UserRestaurant, Long> {
}
