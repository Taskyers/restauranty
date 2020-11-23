package pl.taskyers.restauranty.web.menu.helper;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.web.menu.dto.MenuGroup;
import pl.taskyers.restauranty.web.menu.dto.MenuGroupWrapper;

import java.util.*;

@UtilityClass
public class MenuHelper {
    
    /**
     * Group menu by {@link DishType}
     *
     * @param menu {@link Set} of {@link SingleMenuDish}
     * @return {@link MenuGroupWrapper} containing grouped menu - {@link Set} of pairs - ({@link DishType}, {@link Set<SingleMenuDish>})
     * @since 1.0.0
     */
    public MenuGroupWrapper groupMenu(@NonNull final Set<SingleMenuDish> menu) {
        final Map<DishType, Set<SingleMenuDish>> groupMapping = new LinkedHashMap<>(DishType.values().length);
        Arrays.stream(DishType.values())
                .forEach(dishType -> groupMapping.put(dishType, new HashSet<>()));
        menu.forEach(singleMenuDish -> groupMapping.merge(singleMenuDish.getType(), Sets.newHashSet(singleMenuDish), (ts, ts2) -> {
            ts.addAll(ts2);
            return Sets.newHashSet(ts);
        }));
        
        final Set<MenuGroup> result = new LinkedHashSet<>(DishType.values().length);
        groupMapping.forEach((dishType, singleMenuDishes) -> result.add(new MenuGroup(dishType, singleMenuDishes)));
        return new MenuGroupWrapper(result);
    }
    
}
