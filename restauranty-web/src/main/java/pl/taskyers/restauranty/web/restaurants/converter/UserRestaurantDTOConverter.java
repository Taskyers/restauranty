package pl.taskyers.restauranty.web.restaurants.converter;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.web.restaurants.dto.UserRestaurantDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class UserRestaurantDTOConverter {
    
    public UserRestaurantDTO convertToDTO(UserRestaurant restaurant) {
        return new UserRestaurantDTO(restaurant.getId(), restaurant.getUsername());
    }
    
    public Set<UserRestaurantDTO> convertToDTOCollection(Collection<UserRestaurant> restaurants) {
        final Set<UserRestaurantDTO> result = new HashSet<>(restaurants.size());
        restaurants.forEach(restaurant -> result.add(convertToDTO(restaurant)));
        return result;
    }
    
}
