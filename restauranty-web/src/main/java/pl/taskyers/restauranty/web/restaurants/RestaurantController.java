package pl.taskyers.restauranty.web.restaurants;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import static pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter.convertToDTO;
import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.Restaurant.RESTAURANT_CREATED;
import static pl.taskyers.restauranty.core.messages.enums.MessageType.SUCCESS;
import static pl.taskyers.restauranty.web.util.UriUtils.createURIFromId;

@RestController
@RequestMapping(value = RestaurantService.RESTAURANT_PREFIX)
@RequiredArgsConstructor
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    @PostMapping
    public ResponseEntity<ResponseMessage<RestaurantDTO>> saveNewRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        Restaurant saved = restaurantService.addRestaurant(restaurantDTO);
        
        RestaurantDTO responseRestaurant = convertToDTO(saved);
        
        return ResponseEntity.created(createURIFromId(saved.getId()))
                .body(new ResponseMessage<>(getMessage(RESTAURANT_CREATED), SUCCESS,
                        responseRestaurant));
    }
    
}
