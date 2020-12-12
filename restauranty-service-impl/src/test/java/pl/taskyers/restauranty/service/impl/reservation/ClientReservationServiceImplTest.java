package pl.taskyers.restauranty.service.impl.reservation;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.open_hour.entity.OpenHour;
import pl.taskyers.restauranty.core.data.reservation.ReservationNotFound;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.utils.DateUtils;
import pl.taskyers.restauranty.repository.reservation.ReservationRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.reservation.validator.ReservationDTOValidator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientReservationServiceImplTest {
    
    private ReservationDTOValidator reservationDTOValidator;
    
    private AuthProvider authProvider;
    
    private ReservationRepository reservationRepository;
    
    private RestaurantRepository restaurantRepository;
    
    private ClientReservationServiceImpl clientReservationService;
    
    @BeforeEach
    public void setUp() {
        authProvider = mock(AuthProvider.class);
        restaurantRepository = mock(RestaurantRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        reservationDTOValidator = new ReservationDTOValidator();
        clientReservationService =
                new ClientReservationServiceImpl(reservationDTOValidator, authProvider, reservationRepository, restaurantRepository);
    }
    
    @Test
    public void testMakingReservationWithInvalidDateAndTimeFormat() {
        //given
        ReservationDTO reservationDTO = new ReservationDTO(1L, "121212", "12:", 2, "Test", ReservationStatus.PENDING, "Test");
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testMakingReservationWithBlankRestaurantNameAndInvalidPersonsCount() {
        //given
        ReservationDTO reservationDTO = new ReservationDTO(1L, getTomorrowsDate(), "12:00", -2, "", ReservationStatus.PENDING, "Test");
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testMakingReservationWithDateBeforeTodayAndInvalidTime() {
        //given
        ReservationDTO reservationDTO = new ReservationDTO(1L, "12-12-2001", "28:99", 2, "Test", ReservationStatus.PENDING, "Test");
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(2));
    }
    
    @Test
    public void testMakingReservationWithNonExistingRestaurant() {
        //given
        String restaurantName = "Test";
        ReservationDTO reservationDTO = new ReservationDTO(1L, getTomorrowsDate(), "20:00", 2, restaurantName, ReservationStatus.PENDING, "Test");
        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.empty());
        //when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessage(), is("Restaurant with name " + restaurantName + " was not found"));
    }
    
    @Test
    public void testMakingReservationWithFullRestaurantCapacity() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setCapacity(5);
        ReservationDTO reservationDTO =
                new ReservationDTO(1L, getTomorrowsDate(), "12:30", 2, restaurant.getName(), ReservationStatus.PENDING, "Test");
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.of(restaurant));
        when(reservationRepository.countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(restaurant, ReservationStatus.ACCEPTED,
                DateUtils.parseDate(getTomorrowsDate()), DateUtils.parseTime("12:00"), DateUtils.parseTime("13:00"))).thenReturn(5);
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testMakingReservationWithInvalidReservationCapacity() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setCapacity(5);
        ReservationDTO reservationDTO =
                new ReservationDTO(1L, getTomorrowsDate(), "12:30", 6, restaurant.getName(), ReservationStatus.PENDING, "Test");
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.of(restaurant));
        when(reservationRepository.countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(restaurant, ReservationStatus.ACCEPTED,
                DateUtils.parseDate(getTomorrowsDate()), DateUtils.parseTime("12:00"), DateUtils.parseTime("13:00"))).thenReturn(2);
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testMakingReservationWithReservationTimeNotBetweenOpenHours() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setCapacity(5);
        DayOfWeek dayOfWeek = LocalDate.parse(
                pl.taskyers.restauranty.core.utils.DateUtils.parseStringDate(org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1)),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).getDayOfWeek();
        OpenHour openHour = new OpenHour(1L, dayOfWeek.getValue(), DateUtils.parseTime("11:00"), DateUtils.parseTime("14:00"), restaurant);
        restaurant.setOpenHours(Sets.newHashSet(openHour));
        ReservationDTO reservationDTO =
                new ReservationDTO(1L, getTomorrowsDate(), "10:00", 1, restaurant.getName(), ReservationStatus.PENDING, "Test");
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.of(restaurant));
        when(reservationRepository.countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(restaurant, ReservationStatus.ACCEPTED,
                DateUtils.parseDate(getTomorrowsDate()), DateUtils.parseTime("12:00"), DateUtils.parseTime("13:00"))).thenReturn(2);
        
        //when
        final ValidationException result = assertThrows(ValidationException.class, () -> clientReservationService.makeReservation(reservationDTO));
        
        //then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testMakingValidReservation() {
        //given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setCapacity(5);
        DayOfWeek dayOfWeek = LocalDate.parse(
                pl.taskyers.restauranty.core.utils.DateUtils.parseStringDate(org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1)),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).getDayOfWeek();
        OpenHour openHour = new OpenHour(1L, dayOfWeek.getValue(), DateUtils.parseTime("11:00"), DateUtils.parseTime("14:00"), restaurant);
        restaurant.setOpenHours(Sets.newHashSet(openHour));
        ReservationDTO reservationDTO =
                new ReservationDTO(1L, getTomorrowsDate(), "12:30", 1, restaurant.getName(), ReservationStatus.PENDING, "Test");
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.of(restaurant));
        when(reservationRepository.countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(restaurant, ReservationStatus.ACCEPTED,
                DateUtils.parseDate(getTomorrowsDate()), DateUtils.parseTime("12:00"), DateUtils.parseTime("13:00"))).thenReturn(2);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final Reservation result = clientReservationService.makeReservation(reservationDTO);
        
        //then
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result, notNullValue());
        assertThat(result.getRestaurant(), is(restaurant));
        assertThat(result.getReservationTime(), is(DateUtils.parseTime("12:30")));
        assertThat(result.getReservationDate(), is(DateUtils.parseDate(getTomorrowsDate())));
    }
    
    @Test
    public void testCancelingNotExsitingReservation() {
        //given
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());
        
        //when
        final ReservationNotFound result = assertThrows(ReservationNotFound.class, () -> clientReservationService.cancelReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation with id " + id + " was not found"));
    }
    
    @Test
    public void testCancelingSomeoneOtherReservation() {
        //given
        Long id = 1L;
        final UserClient user = new UserClient();
        user.setUsername("test");
        Reservation reservation = new Reservation();
        reservation.setClient(user);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("bad");
        
        //when
        final ForbiddenException result = assertThrows(ForbiddenException.class, () -> clientReservationService.cancelReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation is not yours"));
    }
    
    
    @Test
    public void testCancelingValidReservation() {
        //given
        Long id = 1L;
        final UserClient user = new UserClient();
        user.setUsername("test");
        Reservation reservation = new Reservation();
        reservation.setClient(user);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("test");
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final Reservation result = clientReservationService.cancelReservation(id);
        
        //then
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result, notNullValue());
        assertThat(result.getStatus(), is(ReservationStatus.CANCELED));
    }
    
    
    private String getTomorrowsDate() {
        return DateUtils.parseStringDate(org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1));
    }
    
}
