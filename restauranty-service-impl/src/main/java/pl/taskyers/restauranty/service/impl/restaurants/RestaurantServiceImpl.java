package pl.taskyers.restauranty.service.impl.restaurants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.openhour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.addresses.AddressRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.restaurants.validator.RestaurantDTOValidator;
import pl.taskyers.restauranty.service.openhour.OpenHourService;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;
import pl.taskyers.restauranty.service.tags.TagService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    
    private final RestaurantDTOValidator restaurantDTOValidator;
    
    private final RestaurantRepository restaurantRepository;
    
    private final AddressRepository addressRepository;
    
    private final AuthProvider authProvider;
    
    private final TagService tagService;
    
    private final OpenHourService openHourService;
    
    @Override
    public Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO) {
        ValidationMessageContainer validationMessageContainer = restaurantDTOValidator.validate(restaurantDTO, true);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        final Set<Tag> tags = tagService.saveAllAndGet(restaurantDTO.getTags());
        Restaurant toSave = RestaurantConverter.convertFromDTO(restaurantDTO, tags, restaurantDTO.getOpenHours());
        Address restaurantAddress = toSave.getAddress();
        
        Address existingAddress = addressRepository.findByStreetAndZipCodeAndCityAndCountry(restaurantAddress.getStreet(),
                restaurantAddress.getZipCode(), restaurantAddress.getCity(), restaurantAddress.getCountry());
        if ( existingAddress != null ) {
            toSave.setAddress(existingAddress);
        }
        
        UserRestaurant userRestaurant = (UserRestaurant) authProvider.getUserEntity();
        toSave.setOwner(userRestaurant);
        
        return restaurantRepository.save(toSave);
    }
    
    @Override
    public Restaurant getRestaurant(@NonNull final Long id) {
        UserBase userBase = authProvider.getUserEntity();
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(
                MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "id", id)));
        if ( !restaurant.getOwner().getUsername().equals(userBase.getUsername()) ) {
            throw new ForbiddenException(MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_YOURS));
        }
        
        return restaurant;
    }
    
    @Override
    public Restaurant getRestaurant(@NonNull String name) {
        return restaurantRepository.findByName(name)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_WITH_FIELD_NOT_FOUND, "name", name)));
    }
    
    @Override
    public List<Restaurant> getUserRestaurants() {
        UserBase userBase = authProvider.getUserEntity();
        List<Restaurant> userRestaurants = restaurantRepository.findAllByOwner(userBase);
        
        return userRestaurants;
    }
    
    @Override
    public Restaurant editRestaurant(@NonNull final Long id, @NonNull RestaurantDTO restaurantDTO) {
        ValidationMessageContainer validationMessageContainer = restaurantDTOValidator.validate(restaurantDTO, false);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        final Set<Tag> tags = tagService.saveAllAndGet(restaurantDTO.getTags());
        Restaurant restaurant = getRestaurant(id);
        final Set<OpenHour> openHours = openHourService.deleteAndSaveAll(restaurant, restaurantDTO.getOpenHours());
        restaurant = updateRestaurantFields(restaurant, restaurantDTO, tags, openHours);
        
        return restaurantRepository.save(restaurant);
    }
    
    @Override
    public void deleteRestaurant(@NonNull final Long id) {
        Restaurant toDelete = getRestaurant(id);
        restaurantRepository.deleteById(toDelete.getId());
    }
    
    @Override
    public boolean restaurantExistsByName(@NonNull String name) {
        return restaurantRepository.findByName(name).isPresent();
    }
    
    @Override
    public boolean restaurantExistsByPhoneNumber(@NonNull String phoneNumber) {
        return restaurantRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
    
    private Restaurant updateRestaurantFields(Restaurant restaurant, RestaurantDTO restaurantDTO, Set<Tag> tags, Set<OpenHour> openHours) {
        restaurant.setName(restaurantDTO.getName());
        AddressDTO addressDTO = restaurantDTO.getAddress();
        Address restaurantAddress = restaurant.getAddress();
        restaurantAddress.setStreet(addressDTO.getStreet());
        restaurantAddress.setCity(addressDTO.getCity());
        restaurantAddress.setCountry(addressDTO.getCountry());
        restaurantAddress.setZipCode(addressDTO.getZipCode());
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
        restaurant.setCapacity(restaurantDTO.getCapacity());
        restaurant.setTags(tags);
        restaurant.setOpenHours(openHours);
        return restaurant;
    }
    
}
