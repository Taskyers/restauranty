package pl.taskyers.restauranty.service.impl.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.reservation.ReservationNotFound;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.repository.reservation.ReservationRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.reservation.validator.ReservationDTOValidator;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RestaurantReservationServiceImplTest {
    
    ReservationDTOValidator reservationDTOValidator;
    
    AuthProvider authProvider;
    
    RestaurantRepository restaurantRepository;
    
    ReservationRepository reservationRepository;
    
    RestaurantReservationServiceImpl restaurantReservationService;
    
    @BeforeEach
    public void setUp() {
        reservationDTOValidator = new ReservationDTOValidator();
        authProvider = mock(AuthProvider.class);
        reservationRepository = mock(ReservationRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        restaurantReservationService =
                new RestaurantReservationServiceImpl(authProvider, restaurantRepository, reservationRepository);
    }
    
    @Test
    public void testGetReservationsNotExistingRestaurant() {
        //given
        String restaurantName = "test";
        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.empty());
        
        //when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> restaurantReservationService.getAllRestaurantReservations(restaurantName));
        
        //then
        assertThat(result.getMessage(), is("Restaurant with name " + restaurantName + " was not found"));
    }
    
    @Test
    public void testGetSomeoneElseReservations() {
        //given
        String restaurantName = "test";
        final UserRestaurant user = new UserRestaurant();
        user.setUsername("test");
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantName);
        restaurant.setOwner(user);
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.of(restaurant));
        when(authProvider.getUserLogin()).thenReturn("bad");
        
        //when
        final ForbiddenException result =
                assertThrows(ForbiddenException.class, () -> restaurantReservationService.getAllRestaurantReservations(restaurantName));
        
        //then
        assertThat(result.getMessage(), is("Restaurant is not yours"));
    }
    
    @Test
    public void testAcceptNotExistingReservation() {
        //given
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());
        
        //when
        final ReservationNotFound result = assertThrows(ReservationNotFound.class, () -> restaurantReservationService.acceptReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation with id " + id + " was not found"));
    }
    
    @Test
    public void testAcceptSomeoneElseReservation() {
        //given
        Long id = 1L;
        final UserRestaurant user = new UserRestaurant();
        user.setUsername("test");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("bad");
        
        //when
        final ForbiddenException result =
                assertThrows(ForbiddenException.class, () -> restaurantReservationService.acceptReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation is not yours"));
    }
    
    @Test
    public void testAcceptValidReservation() {
        //given
        Long id = 1L;
        final UserRestaurant user = new UserRestaurant();
        user.setUsername("test");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("test");
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final Reservation result = restaurantReservationService.acceptReservation(id);
        
        //then
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result.getStatus(), is(ReservationStatus.ACCEPTED));
    }
    
    @Test
    public void testRejectNotExistingReservation() {
        //given
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());
        
        //when
        final ReservationNotFound result = assertThrows(ReservationNotFound.class, () -> restaurantReservationService.rejectReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation with id " + id + " was not found"));
    }
    
    @Test
    public void testRejectSomeoneElseReservation() {
        //given
        Long id = 1L;
        final UserRestaurant user = new UserRestaurant();
        user.setUsername("test");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("bad");
        
        //when
        final ForbiddenException result =
                assertThrows(ForbiddenException.class, () -> restaurantReservationService.rejectReservation(id));
        
        //then
        assertThat(result.getMessage(), is("Reservation is not yours"));
    }
    
    @Test
    public void testRejectValidReservation() {
        //given
        Long id = 1L;
        final UserRestaurant user = new UserRestaurant();
        user.setUsername("test");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));
        when(authProvider.getUserLogin()).thenReturn("test");
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        //when
        final Reservation result = restaurantReservationService.rejectReservation(id);
        
        //then
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result.getStatus(), is(ReservationStatus.REJECTED));
    }
    
}
