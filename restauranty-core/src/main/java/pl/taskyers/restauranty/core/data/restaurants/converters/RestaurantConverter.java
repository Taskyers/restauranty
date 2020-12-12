package pl.taskyers.restauranty.core.data.restaurants.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.open_hour.dto.OpenHourDTO;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;
import pl.taskyers.restauranty.core.utils.DateUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RestaurantConverter {
    
    public Restaurant convertFromDTO(RestaurantDTO restaurantDTO, Set<Tag> tags, Set<OpenHourDTO> openHours) {
        Restaurant restaurant = new Restaurant();
        AddressDTO restaurantAddress = restaurantDTO.getAddress();
        Address address = new Address();
        address.setStreet(restaurantAddress.getStreet());
        address.setCity(restaurantAddress.getCity());
        address.setCountry(restaurantAddress.getCountry());
        address.setZipCode(restaurantAddress.getZipCode());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setCapacity(restaurantDTO.getCapacity());
        restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
        restaurant.setAddress(address);
        restaurant.setTags(tags);
        restaurant.setOpenHours(convertOpenHoursDTO(restaurant, openHours));
        return restaurant;
    }
    
    public RestaurantDTO convertToDTO(Restaurant restaurant) {
        AddressDTO addressDTO = new AddressDTO();
        Address restaurantAddress = restaurant.getAddress();
        addressDTO.setStreet(restaurantAddress.getStreet());
        addressDTO.setCity(restaurantAddress.getCity());
        addressDTO.setCountry(restaurantAddress.getCountry());
        addressDTO.setZipCode(restaurantAddress.getZipCode());
        return new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), restaurant.getCapacity(), addressDTO,
                restaurant.getPhoneNumber(),
                convertTags(restaurant.getTags()), convertOpenHours(restaurant.getOpenHours()));
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
    
    private Set<OpenHourDTO> convertOpenHours(Set<OpenHour> openHours) {
        Set<OpenHourDTO> result = new HashSet<>();
        for ( OpenHour openHour : openHours ) {
            result.add(new OpenHourDTO(DayOfWeek.of(openHour.getDayOfWeek()).name(), DateUtils.parseStringTime(openHour.getOpenTime()),
                    DateUtils.parseStringTime(openHour.getCloseTime())));
        }
        return result;
    }
    
    private Set<OpenHour> convertOpenHoursDTO(Restaurant restaurant, Set<OpenHourDTO> openHours) {
        Set<OpenHour> result = new HashSet<>();
        for ( OpenHourDTO openHour : openHours ) {
            result.add(OpenHour.builder()
                    .dayOfWeek(DayOfWeek.valueOf(openHour.getDayOfWeek().toUpperCase()).getValue())
                    .openTime(DateUtils.parseTime(openHour.getOpenTime()))
                    .closeTime(DateUtils.parseTime(openHour.getCloseTime()))
                    .restaurant(restaurant)
                    .build());
        }
        return result;
    }
    
}
