package pl.taskyers.restauranty.service.impl.restaurants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
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
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    
    private final RestaurantDTOValidator restaurantDTOValidator;
    
    private final RestaurantRepository restaurantRepository;
    
    private final AddressRepository addressRepository;
    
    private final AuthProvider authProvider;
    
    @Override
    public Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO) {
        
        ValidationMessageContainer validationMessageContainer = restaurantDTOValidator.validate(restaurantDTO, true);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        Restaurant toSave = RestaurantConverter.convertFromDTO(restaurantDTO);
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
            throw new ForbiddenException(MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_YOURS, "id", id));
        }
        
        return restaurant;
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
        Restaurant restaurant = getRestaurant(id);
        restaurant = updateRestaurantFields(restaurant, restaurantDTO);
        
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
    
    private Restaurant updateRestaurantFields(Restaurant restaurant, RestaurantDTO restaurantDTO){
        restaurant.setName(restaurantDTO.getName());
        AddressDTO addressDTO = restaurantDTO.getAddress();
        Address restaurantAddress = restaurant.getAddress();
        restaurantAddress.setStreet(addressDTO.getStreet());
        restaurantAddress.setCity(addressDTO.getCity());
        restaurantAddress.setCountry(addressDTO.getCountry());
        restaurantAddress.setZipCode(addressDTO.getZipCode());
        restaurant.setAddress(restaurantAddress);
        restaurant.setPhoneNumber(restaurantDTO.getPhoneNumber());
        return restaurant;
    }
}
