package pl.taskyers.restauranty.core.data.addresses.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    
    private String street;
    
    private String zipCode;
    
    private String city;
    
    private String country;
    
}
