package pl.taskyers.restauranty.service.restaurants;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

import java.util.Collection;

public interface AdminRestaurantService {
    
    String PREFIX = "/admin/restaurants";
    
    String VERIFY = "/verify";
    
    String BY_ID = "/{id}";
    
    /**
     * Get all not verified restaurants ({@link UserRestaurant#verified is set to false})
     *
     * @return {@link Collection} of restaurant's users
     * @since 1.0.0
     */
    Collection<UserRestaurant> getNotVerifiedRestaurants();
    
    /**
     * Verify restaurant account ({@link UserRestaurant#verified set to true})
     *
     * @param id account's id
     * @return updated {@link UserRestaurant}
     * @throws UserNotFoundException if restaurant user was not found
     * @since 1.0.0
     */
    UserRestaurant verify(@NonNull final Long id) throws UserNotFoundException;
    
}
