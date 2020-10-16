package pl.taskyers.restauranty.service.impl.restaurants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.restaurants.converters.RestaurantConverter;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.repository.addresses.AddressRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.impl.restaurants.validator.RestaurantDTOValidator;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    
    private final RestaurantDTOValidator restaurantDTOValidator;
    
    private final RestaurantRepository restaurantRepository;
    
    private final AddressRepository addressRepository;
    
    @Override
    public Restaurant addRestaurant(@NonNull RestaurantDTO restaurantDTO) {
        
        ValidationMessageContainer validationMessageContainer = restaurantDTOValidator.validate(restaurantDTO);
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
        
        return restaurantRepository.save(toSave);
    }
    
}
