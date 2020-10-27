package pl.taskyers.restauranty.web.restaurants;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.List;

import static pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter.convertToDTO;
import static pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter.convertToDTOList;
import static pl.taskyers.restauranty.core.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.Restaurant.RESTAURANT_CREATED;
import static pl.taskyers.restauranty.core.messages.enums.MessageCode.Restaurant.RESTAURANT_UPDATED;
import static pl.taskyers.restauranty.core.messages.enums.MessageType.SUCCESS;
import static pl.taskyers.restauranty.web.util.UriUtils.createURIFromId;

@RestController
@RequestMapping(RestaurantService.RESTAURANT_PREFIX)
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
    
    @GetMapping(RestaurantService.GET_RESTAURANT_BY_ID)
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable Long id) {
        Restaurant toFind = restaurantService.getRestaurant(id);
        
        RestaurantDTO responseRestaurant = convertToDTO(toFind);
        
        return ResponseEntity.ok(responseRestaurant);
    }
    
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getUserRestaurants() {
        List<Restaurant> toFind = restaurantService.getUserRestaurants();
        
        return ResponseEntity.ok(convertToDTOList(toFind));
    }
    
    @PutMapping(RestaurantService.GET_RESTAURANT_BY_ID)
    public ResponseEntity<ResponseMessage<RestaurantDTO>> editRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        Restaurant edited = restaurantService.editRestaurant(id, restaurantDTO);
        
        RestaurantDTO responseRestaurant = convertToDTO(edited);
        
        return ResponseEntity.ok(new ResponseMessage<>(getMessage(RESTAURANT_UPDATED), SUCCESS,
                responseRestaurant));
    }
    
    @DeleteMapping(RestaurantService.GET_RESTAURANT_BY_ID)
    public ResponseEntity<ResponseMessage<String>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Restaurant.RESTAURANT_DELETED, MessageType.SUCCESS));
    }
    
    @GetMapping(RestaurantService.GET_RESTAURANT_BY_NAME)
    public boolean restaurantExistsByName(@PathVariable String name) {
        return restaurantService.restaurantExistsByName(name);
    }
    
    @GetMapping(RestaurantService.GET_RESTAURANT_BY_PHONE_NUMBER)
    public boolean restaurantExistsByPhoneNumber(@PathVariable String phoneNumber) {
        return restaurantService.restaurantExistsByPhoneNumber(phoneNumber);
    }
    
}
