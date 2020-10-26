package pl.taskyers.restauranty.core.data.restaurants.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant implements Serializable {
    
    private static final long serialVersionUID = 3399824192679467708L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToOne(targetEntity = Address.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(nullable = false, name = "address")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Address address;
    
    @OneToMany(targetEntity = RestaurantImage.class, mappedBy = "restaurant", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<RestaurantImage> images = new HashSet<>();
    
    @OneToMany(targetEntity = Review.class, mappedBy = "restaurant", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();
    
    @Column(name = "phone_number", nullable = false, unique = true, length = 9)
    private String phoneNumber;
    
    @ManyToOne(targetEntity = UserRestaurant.class)
    @JoinColumn(name = "owner")
    private UserRestaurant owner;
    
}
