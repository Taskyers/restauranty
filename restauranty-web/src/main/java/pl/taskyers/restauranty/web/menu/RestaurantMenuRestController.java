package pl.taskyers.restauranty.web.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.menu.MenuService;
import pl.taskyers.restauranty.service.menu.RestaurantMenuService;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;
import pl.taskyers.restauranty.web.menu.dto.MenuGroup;
import pl.taskyers.restauranty.web.menu.helper.MenuHelper;
import pl.taskyers.restauranty.web.util.UriUtils;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(MenuService.RESTAURANT_PREFIX)
public class RestaurantMenuRestController {
    
    private final MenuService menuService;
    
    private final RestaurantMenuService restaurantMenuService;
    
    @GetMapping(MenuService.BY_RESTAURANT)
    public ResponseEntity<Set<MenuGroup>> getMenuForRestaurant(@PathVariable final String restaurant) {
        final Set<SingleMenuDish> menu = menuService.getMenuForRestaurant(restaurant);
        return ResponseEntity.ok(MenuHelper.groupMenu(menu)
                .getGroups());
    }
    
    @GetMapping(MenuService.BY_RESTAURANT + MenuService.BY_TYPE)
    public ResponseEntity<Set<MenuGroup>> getMenuForRestaurantAndType(@PathVariable final String restaurant, @PathVariable final String type) {
        final Set<SingleMenuDish> menu = menuService.getDishesForRestaurantAndType(restaurant, DishType.valueOf(type));
        return ResponseEntity.ok(MenuHelper.groupMenu(menu)
                .getGroups());
    }
    
    @PostMapping(MenuService.BY_RESTAURANT)
    public ResponseEntity<ResponseMessage<SingleMenuDish>> addDishToMenu(@PathVariable final String restaurant,
            @RequestBody final AddDishDTO addDishDTO) {
        final SingleMenuDish savedDish = restaurantMenuService.addDishToMenu(restaurant, addDishDTO);
        return ResponseEntity.created(UriUtils.createURIFromId(savedDish.getId()))
                .body(new ResponseMessage<>(MessageCode.Menu.DISH_ADDED, MessageType.SUCCESS, savedDish));
    }
    
    @PutMapping(MenuService.BY_RESTAURANT + MenuService.BY_ID)
    public ResponseEntity<ResponseMessage<SingleMenuDish>> editDish(@PathVariable final String restaurant, @PathVariable final Long id,
            @RequestBody final AddDishDTO addDishDTO) {
        final SingleMenuDish editedDish = restaurantMenuService.editDish(id, restaurant, addDishDTO);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Menu.DISH_EDITED, MessageType.SUCCESS, editedDish));
    }
    
    @DeleteMapping(MenuService.BY_RESTAURANT + MenuService.BY_ID)
    public ResponseEntity<ResponseMessage<?>> removeDish(@PathVariable final String restaurant, @PathVariable final Long id) {
        restaurantMenuService.removeDish(id, restaurant);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Menu.DISH_REMOVED, MessageType.SUCCESS));
    }
    
}
