package pl.taskyers.restauranty.service.impl.restaurant;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.addresses.dto.AddressDTO;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.openhour.dto.OpenHourDTO;
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
import pl.taskyers.restauranty.service.impl.openhour.validator.OpenHourDTOValidator;
import pl.taskyers.restauranty.service.impl.restaurants.RestaurantServiceImpl;
import pl.taskyers.restauranty.service.impl.restaurants.validator.RestaurantDTOValidator;
import pl.taskyers.restauranty.service.openhour.OpenHourService;
import pl.taskyers.restauranty.service.tags.TagService;

import java.time.DayOfWeek;
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
    
    private static final Set<OpenHourDTO> OPEN_HOUR_DTOS = Sets.newHashSet(new OpenHourDTO(DayOfWeek.MONDAY.name(), "12:00", "13:00"));
    
    private RestaurantServiceImpl restaurantService;
    
    private RestaurantDTOValidator restaurantDTOValidator;
    
    private RestaurantRepository restaurantRepository;
    
    private AddressRepository addressRepository;
    
    private AddressDTOValidator addressDTOValidator;
    
    private OpenHourDTOValidator openHourDTOValidator;
    
    private AuthProvider authProvider;
    
    @BeforeEach
    public void setUp() {
        TagService tagService = mock(TagService.class);
        OpenHourService openHourService = mock(OpenHourService.class);
        restaurantRepository = mock(RestaurantRepository.class);
        addressRepository = mock(AddressRepository.class);
        authProvider = mock(AuthProvider.class);
        addressDTOValidator = new AddressDTOValidator();
        openHourDTOValidator = new OpenHourDTOValidator();
        restaurantDTOValidator = new RestaurantDTOValidator(restaurantRepository, addressDTOValidator, openHourDTOValidator);
        restaurantService =
                new RestaurantServiceImpl(restaurantDTOValidator, restaurantRepository, addressRepository, authProvider, tagService, openHourService);
    }
    
    @Test
    public void testAddingRestaurantWithBlankFields() {
        //given
        AddressDTO addressDTO = new AddressDTO("", "", "", "");
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "", "", 3, addressDTO, "", TAGS, OPEN_HOUR_DTOS);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(7));
    }
    
    @Test
    public void testAddingRestaurantWithExistingNameAndExistingPhone() {
        //given
        Restaurant restaurant = new Restaurant();
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String existingName = "Test";
        String existingPhoneNumber = "997998999";
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, existingName, "Test desc", 3, addressDTO, existingPhoneNumber, TAGS, OPEN_HOUR_DTOS);
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
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, "Test", "Test desc", 3, addressDTO, existingPhoneNumber, TAGS, OPEN_HOUR_DTOS);
        when(restaurantRepository.findByPhoneNumber(existingPhoneNumber)).thenReturn(Optional.of(restaurant));
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(4));
    }
    
    @Test
    public void testAddingRestaurantWithOpenTimeAfterCloseTime() {
        //given
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String name = "Test";
        String phoneNumber = "997998999";
        Set<OpenHourDTO> invalidOpenTimes = Sets.newHashSet(new OpenHourDTO(DayOfWeek.MONDAY.name(), "14:00", "13:00"));
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, name, "Test desc", 3, addressDTO, phoneNumber, TAGS, invalidOpenTimes);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testAddingRestaurantWithInvalidCloseTimeFormat() {
        //given
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String name = "Test";
        String phoneNumber = "997998999";
        Set<OpenHourDTO> invalidOpenTimes = Sets.newHashSet(new OpenHourDTO(DayOfWeek.MONDAY.name(), "12:00", "13:"));
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, name, "Test desc", 3, addressDTO, phoneNumber, TAGS, invalidOpenTimes);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testAddingRestaurantWithBlankDayOfWeekAndBlankOpenTimeAndCloseTimeFormat() {
        //given
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String name = "Test";
        String phoneNumber = "997998999";
        Set<OpenHourDTO> invalidOpenTimes = Sets.newHashSet(new OpenHourDTO("", "", "13:00"));
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, name, "Test desc", 3, addressDTO, phoneNumber, TAGS, invalidOpenTimes);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testAddingRestaurantWithNotExistingDayOfWeekAndBlankOpenTime() {
        //given
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String name = "Test";
        String phoneNumber = "997998999";
        Set<OpenHourDTO> invalidOpenTimes = Sets.newHashSet(new OpenHourDTO("test", "", "13:00"));
        RestaurantDTO restaurantDTO = new RestaurantDTO(1L, name, "Test desc", 3, addressDTO, phoneNumber, TAGS, invalidOpenTimes);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> restaurantService.addRestaurant(restaurantDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
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
        assertThat(result.getMessage(), is("Restaurant is not yours"));
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
        assertThat(result.getMessage(), is("Restaurant is not yours"));
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
        assertThat(result.getMessage(), is("Restaurant is not yours"));
    }
    
    private RestaurantDTO getValidRestaurantDTO() {
        AddressDTO addressDTO = new AddressDTO(VALID_STREET, VALID_ZIP_CODE, VALID_CITY_COUNTRY, VALID_CITY_COUNTRY);
        String restaurantName = "Test";
        String restaurantDescription = "Test desc";
        String restaurantPhoneNumber = "997998999";
        return new RestaurantDTO(1L, restaurantName, restaurantDescription, 3, addressDTO, restaurantPhoneNumber, Sets.newHashSet("tag"),
                OPEN_HOUR_DTOS);
    }
    
}
