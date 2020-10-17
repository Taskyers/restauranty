package pl.taskyers.restauranty.service.impl.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.addresses.AddressRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.impl.addresses.validator.AddressDTOValidator;
import pl.taskyers.restauranty.service.impl.restaurants.RestaurantServiceImpl;
import pl.taskyers.restauranty.service.impl.restaurants.validator.RestaurantDTOValidator;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RestaurantServiceImplTest {
    
    private static final String VALID_STREET = "Test 1";
    
    private static final String VALID_CITY_COUNTRY = "Test";
    
    private static final String INVALID_CITY_COUNTRY = "test";
    
    private static final String VALID_ZIP_CODE = "69-420";
    
    private static final String INVALID_ZIP_CODE = "997";
    
    private RestaurantServiceImpl restaurantService;
    
    private RestaurantDTOValidator restaurantDTOValidator;
    
    private RestaurantRepository restaurantRepository;
    
    private AddressRepository addressRepository;
    
    private AddressDTOValidator addressDTOValidator;
    
    @BeforeEach
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        addressRepository = mock(AddressRepository.class);
        addressDTOValidator = new AddressDTOValidator();
        restaurantDTOValidator = new RestaurantDTOValidator(restaurantRepository, addressDTOValidator);
        restaurantService = new RestaurantServiceImpl(restaurantDTOValidator, restaurantRepository, addressRepository);
    }
    
    @Test
    public void testAddingRestaurantWithBlankFields() {
        //given
        AddressDTO addressDTO = new AddressDTO("", "", "", "");
        RestaurantDTO restaurantDTO = new RestaurantDTO("", addressDTO, "");
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(6));
    }
    
    @Test
    public void testAddingRestaurantWithExistingNameAndExistingPhone() {
        //given
        Restaurant restaurant = new Restaurant();
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String existingName = "Test";
        String existingPhoneNumber = "997998999";
        RestaurantDTO restaurantDTO = new RestaurantDTO(existingName, addressDTO, existingPhoneNumber);
        when(restaurantRepository.findByName(existingName)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.findByPhoneNumber(existingPhoneNumber)).thenReturn(Optional.of(restaurant));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testAddingRestaurantWithInvalidZipCodeAndInvalidCityAndInvalidCountryAndExistingPhone() {
        //given
        Restaurant restaurant = new Restaurant();
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, INVALID_ZIP_CODE, INVALID_CITY_COUNTRY, INVALID_CITY_COUNTRY);
        String existingPhoneNumber = "997998999";
        RestaurantDTO restaurantDTO = new RestaurantDTO("Test", addressDTO, existingPhoneNumber);
        when(restaurantRepository.findByPhoneNumber(existingPhoneNumber)).thenReturn(Optional.of(restaurant));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(4));
    }
    
    @Test
    public void testAddingValidRestaurant() {
        //given
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String restaurantName = "Test";
        String restaurantPhoneNumber = "997998999";
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantName, addressDTO, restaurantPhoneNumber);
        
        //when
        restaurantService.addRestaurant(restaurantDTO);
        
        //then
        verify(addressRepository).findByStreetAndZipCodeAndCityAndCountry(anyString(), anyString(), anyString(), anyString());
        verify(restaurantRepository).save(any(Restaurant.class));
    }
    
    
}
