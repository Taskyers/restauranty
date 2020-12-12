package pl.taskyers.restauranty.core.data.open_hour.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "open_hour")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenHour implements Serializable {
    
    private static final long serialVersionUID = -7465098069324147719L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, name = "day_of_week")
    private int dayOfWeek;
    
    @Column(name = "open_time")
    @Temporal(TemporalType.TIME)
    private Date openTime;
    
    @Column(name = "close_time")
    @Temporal(TemporalType.TIME)
    private Date closeTime;
    
    @ManyToOne(targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;
    
}
