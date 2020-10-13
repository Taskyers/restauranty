package pl.taskyers.restauranty.core.data.users.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_restaurant")
@Data
@Builder
public class UserRestaurant extends UserBase {
    
    private static final long serialVersionUID = -5435621538416089555L;
    
    public UserRestaurant() {
        super();
    }
    
    public UserRestaurant(Long id, Role role, String username, String password, String email) {
        super(id, role, username, password, email);
    }
    
}
