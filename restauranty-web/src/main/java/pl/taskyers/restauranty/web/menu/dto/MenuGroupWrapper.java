package pl.taskyers.restauranty.web.menu.dto;

import lombok.NonNull;
import lombok.Value;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;

import java.util.Set;

@Value
public class MenuGroupWrapper {
    
    Set<MenuGroup> groups;
    
    public Set<SingleMenuDish> getByDishType(@NonNull DishType dishType) {
        final MenuGroup group = groups.stream()
                .filter(menuGroup -> menuGroup.getType() == dishType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Type %s was not found", dishType)));
        return group.getDishes();
    }
    
}
