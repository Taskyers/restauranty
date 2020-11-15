package pl.taskyers.restauranty.core.data.images.entity;

import lombok.*;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_image")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantImage implements Serializable {
    
    private static final long serialVersionUID = 6214626640837242080L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Restaurant restaurant;
    
    @Column(columnDefinition = "boolean default false")
    private boolean main;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String path;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private long size;
    
}
