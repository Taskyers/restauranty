package pl.taskyers.restauranty.service.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDishDTO {
    
    private String name;
    
    private String description;
    
    private Double price;
    
    private DishType type;
    
    public SingleMenuDish toEntity() {
        return SingleMenuDish.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
                .build();
    }
    
}
