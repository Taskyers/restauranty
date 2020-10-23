package pl.taskyers.restauranty.core.data.restaurants.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RestaurantConverter {
    
    public Restaurant convertFromDTO(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        AddressDTO restaurantAddress = restaurantDTO.getAddress();
        Address address = new Address();
        address.setStreet(restaurantAddress.getStreet());
        address.setCity(restaurantAddress.getCity());
        address.setCountry(restaurantAddress.getCountry());
        address.setZipCode(restaurantAddress.getZipCode());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
        restaurant.setAddress(address);
        return restaurant;
    }
    
    public RestaurantDTO convertToDTO(Restaurant restaurant) {
        AddressDTO addressDTO = new AddressDTO();
        Address restaurantAddress = restaurant.getAddress();
        addressDTO.setStreet(restaurantAddress.getStreet());
        addressDTO.setCity(restaurantAddress.getCity());
        addressDTO.setCountry(restaurantAddress.getCountry());
        addressDTO.setZipCode(restaurantAddress.getZipCode());
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), addressDTO, restaurant.getPhoneNumber());
    }
    
    public List<RestaurantDTO> convertToDTOList(List<Restaurant> restaurants) {
        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        for ( Restaurant restaurant : restaurants ) {
            restaurantDTOS.add(convertToDTO(restaurant));
        }
        return restaurantDTOS;
    }
    
}
