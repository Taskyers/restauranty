package pl.taskyers.restauranty.service.impl.clients;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.clients.RestaurantSearchService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantSearchServiceImpl implements RestaurantSearchService {
    
    private final RestaurantRepository restaurantRepository;
    
    @Override
    public Set<String> searchForRestaurants(@NonNull final String restaurantName, @NonNull final Set<String> tags) {
        if ( StringUtils.isBlank(restaurantName) && tags.isEmpty() ) {
            return getAllNames(restaurantRepository.findAll());
        } else if ( tags.isEmpty() ) {
            return getAllNames(restaurantRepository.findAllByNameContainingIgnoreCase(restaurantName));
        } else if ( StringUtils.isBlank(restaurantName) ) {
            return getAllNames(restaurantRepository.findAllByTagsValueIsIn(tags));
        }
        return getAllNames(restaurantRepository.findAllByNameContainingIgnoreCaseAndTagsValueIsIn(restaurantName, tags));
    }
    
    private Set<String> getAllNames(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getOwner()
                        .isVerified())
                .map(Restaurant::getName)
                .collect(Collectors.toSet());
    }
    
}
