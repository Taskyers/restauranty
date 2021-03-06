package pl.taskyers.restauranty.core.data.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_restaurant")
@Data
public class UserRestaurant extends UserBase {
    
    private static final long serialVersionUID = -5435621538416089555L;
    
    @Column(columnDefinition = "boolean default false")
    private boolean verified;
    
    @OneToMany(targetEntity = Restaurant.class, mappedBy = "owner", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<Restaurant> restaurants = new HashSet<>();
    
    public UserRestaurant() {
        super();
    }
    
    public UserRestaurant(Set<Restaurant> restaurants, boolean verified) {
        super();
        this.restaurants = restaurants;
        this.verified = verified;
    }
    
    public UserRestaurant(Long id, Role role, String username, String password, String email, boolean enabled, Set<Restaurant> restaurants,
            boolean verified) {
        super(id, role, username, password, email, enabled);
        this.restaurants = restaurants;
        this.verified = verified;
    }
    
}
