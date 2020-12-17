package pl.taskyers.restauranty.web.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.openhour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.openhour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.service.clients.RestaurantSearchService;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;
import pl.taskyers.restauranty.service.tags.TagService;
import pl.taskyers.restauranty.web.clients.dto.RestaurantSearchDTO;

import java.util.Set;

@RestController
@RequestMapping(RestaurantSearchService.PREFIX)
@RequiredArgsConstructor
public class ClientMainRestController {
    
    private final RestaurantSearchService restaurantSearchService;
    
    private final TagService tagService;
    
    private final RestaurantService restaurantService;
    
    @GetMapping(RestaurantSearchService.SEARCH)
    public ResponseEntity<Set<String>> getAllTags() {
        return ResponseEntity.ok(tagService.getAll());
    }
    
    @PostMapping(RestaurantSearchService.SEARCH)
    public ResponseEntity<Set<String>> searchForRestaurants(@RequestBody final RestaurantSearchDTO restaurantSearchDTO) {
        return ResponseEntity.ok(
                restaurantSearchService.searchForRestaurants(restaurantSearchDTO.getRestaurantName(), restaurantSearchDTO.getTags()));
    }
    
    @GetMapping(RestaurantSearchService.GET_RESTAURANTS_OPEN_HOURS)
    public ResponseEntity<Set<OpenHourDTO>> getRestaurantsOpenHours(@PathVariable String restaurantName) {
        Set<OpenHour> toFind = restaurantService.getRestaurant(restaurantName).getOpenHours();
        return ResponseEntity.ok(RestaurantConverter.convertOpenHours(toFind));
    }
    
    @GetMapping(RestaurantSearchService.GET_RESTAURANT_OWNER)
    public ResponseEntity<String> getRestaurantOwner(@PathVariable String restaurantName) {
        Restaurant toFind = restaurantService.getRestaurant(restaurantName);
        String ownerName = toFind.getOwner().getUsername();
        return ResponseEntity.ok(ownerName);
    }
    
}
