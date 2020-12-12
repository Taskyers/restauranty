package pl.taskyers.restauranty.core.data.restaurants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private int capacity;
    
    private AddressDTO address;
    
    private String phoneNumber;
    
    private Set<String> tags;
    
    private Set<OpenHourDTO> openHours;
    
}
