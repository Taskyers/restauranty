package pl.taskyers.restauranty.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findAllByClient(UserClient client);
    
    List<Reservation> findAllByRestaurant(Restaurant restaurant);
    
    List<Reservation> findAllByRestaurantAndStatus(Restaurant restaurant, ReservationStatus status);
    
    int countByRestaurantAndStatusAndReservationDateAndReservationTimeBetween(Restaurant restaurant, ReservationStatus status, Date reservationDate, Date startTime, Date endTime);
    
    
}
