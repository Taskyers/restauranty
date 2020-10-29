package pl.taskyers.restauranty.service.impl.restaurant;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.dto.RestaurantDTO;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.addresses.AddressRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.addresses.validator.AddressDTOValidator;
import pl.taskyers.restauranty.service.impl.restaurants.RestaurantServiceImpl;
import pl.taskyers.restauranty.service.impl.restaurants.validator.RestaurantDTOValidator;
import pl.taskyers.restauranty.service.tags.TagService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
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
    
    private static final Long ID = 1L;
    
    private static final Set<String> TAGS = Sets.newHashSet("tag");
    
    private RestaurantServiceImpl restaurantService;
    
    private RestaurantDTOValidator restaurantDTOValidator;
    
    private RestaurantRepository restaurantRepository;
    
    private AddressRepository addressRepository;
    
    private AddressDTOValidator addressDTOValidator;
    
    private AuthProvider authProvider;
    
    @BeforeEach
    public void setUp() {
        TagService tagService = mock(TagService.class);
        restaurantRepository = mock(RestaurantRepository.class);
        addressRepository = mock(AddressRepository.class);
        authProvider = mock(AuthProvider.class);
        addressDTOValidator = new AddressDTOValidator();
        restaurantDTOValidator = new RestaurantDTOValidator(restaurantRepository, addressDTOValidator);
        restaurantService = new RestaurantServiceImpl(restaurantDTOValidator, restaurantRepository, addressRepository, authProvider, tagService);
    }
    
    @Test
    public void testAddingRestaurantWithBlankFields() {
        //given
        AddressDTO addressDTO = new AddressDTO("", "", "", "");
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "", addressDTO, "", TAGS);
        
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
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, existingName, addressDTO, existingPhoneNumber, TAGS);
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
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "Test", addressDTO, existingPhoneNumber, TAGS);
        when(restaurantRepository.findByPhoneNumber(existingPhoneNumber)).thenReturn(Optional.of(restaurant));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(4));
    }
    
    @Test
    public void testAddingValidRestaurant() {
        //given
        RestaurantDTO restaurantDTO = getValidRestaurantDTO();
        when(authProvider.getUserEntity()).thenReturn(any(UserBase.class));
        
        //when
        restaurantService.addRestaurant(restaurantDTO);
        
        //then
        verify(addressRepository).findByStreetAndZipCodeAndCityAndCountry(anyString(), anyString(), anyString(), anyString());
        verify(restaurantRepository).save(any(Restaurant.class));
    }
    
    @Test
    public void testGetNotExistingRestaurant() {
        //given
        when(authProvider.getUserEntity()).thenReturn(any(UserBase.class));
        when(restaurantRepository.findById(ID)).thenReturn(null);
        
        //when
        final RestaurantNotFoundException result = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.getRestaurant(ID));
        
        //then
        assertThat(result.getMessage(), is("Restaurant with id " + ID + " was not found"));
    }
    
    @Test
    public void testGetExistingRestaurant() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(userBase);
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        final Restaurant result = restaurantService.getRestaurant(ID);
        
        //then
        assertThat(result, is(restaurant));
    }
    
    @Test
    public void testGetNotYoursRestaurant() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        UserRestaurant restaurantOwner = new UserRestaurant();
        restaurantOwner.setUsername("Bad");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(restaurantOwner);
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        final ForbiddenException result = assertThrows(ForbiddenException.class, () -> restaurantService.getRestaurant(ID));
        
        //then
        assertThat(result.getMessage(), is(String.format("Restaurant with %s %s is not yours", "id", ID)));
    }
    
    @Test
    public void testGetAllUserRestaurants() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        List<Restaurant> restaurants = Arrays.asList(new Restaurant(), new Restaurant());
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findAllByOwner(userBase)).thenReturn(restaurants);
        
        //when
        List<Restaurant> userRestaurants = restaurantService.getUserRestaurants();
        
        //then
        assertThat(userRestaurants.size(), is(2));
    }
    
    @Test
    public void testDeleteNotExistingRestaurant() {
        //given
        when(authProvider.getUserEntity()).thenReturn(any(UserBase.class));
        when(restaurantRepository.findById(ID)).thenReturn(null);
        
        //when
        final RestaurantNotFoundException result = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.deleteRestaurant(ID));
        
        //then
        assertThat(result.getMessage(), is("Restaurant with id " + ID + " was not found"));
    }
    
    @Test
    public void testEditExistingRestaurant() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ID);
        restaurant.setOwner(userBase);
        Address address = new Address(ID, "Test 1", "12345", "Test", "Test");
        restaurant.setAddress(address);
        RestaurantDTO restaurantDTO = getValidRestaurantDTO();
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        restaurantService.editRestaurant(ID, restaurantDTO);
        
        //then
        verify(restaurantRepository).save(any(Restaurant.class));
    }
    
    @Test
    public void testEditExistingRestaurantWithInvalidPhone() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ID);
        restaurant.setOwner(userBase);
        Address address = new Address(ID, "Test 1", "12345", "Test", "Test");
        restaurant.setAddress(address);
        RestaurantDTO restaurantDTO = getValidRestaurantDTO();
        restaurantDTO.setPhoneNumber("INVALID");
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.editRestaurant(ID, restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testEditNotExistingRestaurant() {
        //given
        RestaurantDTO restaurantDTO = getValidRestaurantDTO();
        when(authProvider.getUserEntity()).thenReturn(any(UserBase.class));
        when(restaurantRepository.findById(ID)).thenReturn(null);
        
        //when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> restaurantService.editRestaurant(ID, restaurantDTO));
        
        //then
        assertThat(result.getMessage(), is("Restaurant with id " + ID + " was not found"));
    }
    
    @Test
    public void testEditNotYoursRestaurant() {
        //given
        RestaurantDTO restaurantDTO = getValidRestaurantDTO();
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        UserRestaurant restaurantOwner = new UserRestaurant();
        restaurantOwner.setUsername("Bad");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(restaurantOwner);
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        final ForbiddenException result = assertThrows(ForbiddenException.class, () -> restaurantService.editRestaurant(ID, restaurantDTO));
        
        //then
        assertThat(result.getMessage(), is(String.format("Restaurant with %s %s is not yours", "id", ID)));
    }
    
    @Test
    public void testDeleteExistingRestaurant() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ID);
        restaurant.setOwner(userBase);
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        restaurantService.deleteRestaurant(ID);
        
        //then
        verify(restaurantRepository).findById(ID);
        verify(restaurantRepository).deleteById(ID);
    }
    
    @Test
    public void testDeleteNotYoursRestaurant() {
        //given
        UserRestaurant userBase = new UserRestaurant();
        userBase.setUsername("Test");
        UserRestaurant restaurantOwner = new UserRestaurant();
        restaurantOwner.setUsername("Bad");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(restaurantOwner);
        when(authProvider.getUserEntity()).thenReturn(userBase);
        when(restaurantRepository.findById(ID)).thenReturn(Optional.of(restaurant));
        
        //when
        final ForbiddenException result = assertThrows(ForbiddenException.class, () -> restaurantService.deleteRestaurant(ID));
        
        //then
        assertThat(result.getMessage(), is(String.format("Restaurant with %s %s is not yours", "id", ID)));
    }
    
    private RestaurantDTO getValidRestaurantDTO() {
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String restaurantName = "Test";
        String restaurantPhoneNumber = "997998999";
        return new RestaurantDTO(1L, restaurantName, addressDTO, restaurantPhoneNumber, Sets.newHashSet("tag"));
    }
    
}
