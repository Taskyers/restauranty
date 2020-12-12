package pl.taskyers.restauranty.core.data.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.reservation.enums.ReservationStatus;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {
    
    private static final long serialVersionUID = -412825400041513682L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date reservationDate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    Date reservationTime;
    
    @Column(nullable = false)
    private int personsCount;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    
    @ManyToOne(targetEntity = UserClient.class)
    @JoinColumn(name = "client")
    private UserClient client;
    
    @ManyToOne(targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;
}
