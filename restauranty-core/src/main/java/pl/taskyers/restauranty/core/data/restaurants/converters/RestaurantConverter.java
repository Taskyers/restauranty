package pl.taskyers.restauranty.core.data.restaurants.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantConverter {
    
    public Restaurant convertFromDTO(RestaurantDTO restaurantDTO, Set<Tag> tags) {
        Restaurant restaurant = new Restaurant();
        AddressDTO restaurantAddress = restaurantDTO.getAddress();
        Address address = new Address();
        address.setStreet(restaurantAddress.getStreet());
        address.setCity(restaurantAddress.getCity());
        address.setCountry(restaurantAddress.getCountry());
        address.setZipCode(restaurantAddress.getZipCode());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
        restaurant.setAddress(address);
        restaurant.setTags(tags);
        return restaurant;
    }
    
    public RestaurantDTO convertToDTO(Restaurant restaurant) {
        AddressDTO addressDTO = new AddressDTO();
        Address restaurantAddress = restaurant.getAddress();
        addressDTO.setStreet(restaurantAddress.getStreet());
        addressDTO.setCity(restaurantAddress.getCity());
        addressDTO.setCountry(restaurantAddress.getCountry());
        addressDTO.setZipCode(restaurantAddress.getZipCode());
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), addressDTO, restaurant.getPhoneNumber(),
                convertTags(restaurant.getTags()));
    }
    
    public List<RestaurantDTO> convertToDTOList(List<Restaurant> restaurants) {
        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();
        for ( Restaurant restaurant : restaurants ) {
            restaurantDTOS.add(convertToDTO(restaurant));
        }
        return restaurantDTOS;
    }
    
    private Set<String> convertTags(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getValue)
                .collect(Collectors.toSet());
    }
    
}
