package pl.taskyers.restauranty.web.menu.dto;

import lombok.Value;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;

import java.util.Set;

@Value
public class MenuGroup {
    
    DishType type;
    
    Set<SingleMenuDish> dishes;
    
}
