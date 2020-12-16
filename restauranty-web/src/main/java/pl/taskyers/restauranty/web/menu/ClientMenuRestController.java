package pl.taskyers.restauranty.web.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.service.menu.MenuService;
import pl.taskyers.restauranty.web.menu.dto.MenuGroup;
import pl.taskyers.restauranty.web.menu.helper.MenuHelper;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(MenuService.CLIENT_PREFIX)
public class ClientMenuRestController {
    
    private final MenuService menuService;
    
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
    
}
