package pl.taskyers.restauranty.web.restaurants;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.restaurants.AdminRestaurantService;
import pl.taskyers.restauranty.web.restaurants.converter.UserRestaurantDTOConverter;
import pl.taskyers.restauranty.web.restaurants.dto.UserRestaurantDTO;

import java.util.Collection;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(AdminRestaurantService.PREFIX)
public class AdminRestaurantRestController {
    
    private final AdminRestaurantService adminRestaurantService;
    
    @GetMapping
    public ResponseEntity<Set<UserRestaurantDTO>> getNotVerifiedRestaurants() {
        final Collection<UserRestaurant> notVerified = adminRestaurantService.getNotVerifiedRestaurants();
        return ResponseEntity.ok(UserRestaurantDTOConverter.convertToDTOCollection(notVerified));
    }
    
    @PatchMapping(AdminRestaurantService.VERIFY + AdminRestaurantService.BY_ID)
    public ResponseEntity<ResponseMessage<UserRestaurantDTO>> verifyRestaurant(@PathVariable final Long id) {
        final UserRestaurant result = adminRestaurantService.verify(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Restaurant.RESTAURANT_VERIFIED, MessageType.SUCCESS,
                UserRestaurantDTOConverter.convertToDTO(result)));
    }
    
}
