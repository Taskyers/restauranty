package pl.taskyers.restauranty.core.data.reservation.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.reservation.dto.ReservationDTO;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ReservationDTOConverter {
    
    public Reservation convertFromDTO(ReservationDTO reservationDTO, UserClient client, Restaurant restaurant) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(DateUtils.parseDate(reservationDTO.getReservationDate()));
        reservation.setReservationTime(DateUtils.parseTime(reservationDTO.getReservationTime()));
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setPersonsCount(reservationDTO.getPersonsCount());
        reservation.setClient(client);
        reservation.setRestaurant(restaurant);
        return reservation;
    }
    
    public ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(),
                DateUtils.parseStringDate(reservation.getReservationDate()), DateUtils.parseStringTime(reservation.getReservationTime()),
                reservation.getPersonsCount(), reservation.getRestaurant().getName(), reservation.getStatus(), reservation.getClient().getUsername());
    }
    
    public List<ReservationDTO> convertToDTOList(List<Reservation> reservations) {
        List<ReservationDTO> reservationDTOS = new ArrayList<>();
        for ( Reservation reservation : reservations ) {
            reservationDTOS.add(convertToDTO(reservation));
        }
        return reservationDTOS;
    }
    
}
