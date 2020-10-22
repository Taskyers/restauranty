package pl.taskyers.restauranty.core.data.restaurants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    
    private Long id;
    
    private String name;
    
    private AddressDTO address;
    
    private String phoneNumber;
    
}
