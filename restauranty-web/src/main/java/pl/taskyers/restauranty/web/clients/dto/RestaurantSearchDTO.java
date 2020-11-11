package pl.taskyers.restauranty.web.clients.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RestaurantSearchDTO {
    
    private String restaurantName;
    
    private Set<String> tags;
    
}
