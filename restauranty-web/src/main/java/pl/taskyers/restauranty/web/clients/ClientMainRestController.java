package pl.taskyers.restauranty.web.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.service.clients.RestaurantSearchService;
import pl.taskyers.restauranty.service.tags.TagService;
import pl.taskyers.restauranty.web.clients.dto.RestaurantSearchDTO;

import java.util.Set;

@RestController
@RequestMapping(RestaurantSearchService.PREFIX)
@RequiredArgsConstructor
public class ClientMainRestController {
    
    private final RestaurantSearchService restaurantSearchService;
    
    private final TagService tagService;
    
    @GetMapping
    public ResponseEntity<Set<String>> getAllTags() {
        return ResponseEntity.ok(tagService.getAll());
    }
    
    @PostMapping
    public ResponseEntity<Set<String>> searchForRestaurants(@RequestBody final RestaurantSearchDTO restaurantSearchDTO) {
        return ResponseEntity.ok(
                restaurantSearchService.searchForRestaurants(restaurantSearchDTO.getRestaurantName(), restaurantSearchDTO.getTags()));
    }
    
}
