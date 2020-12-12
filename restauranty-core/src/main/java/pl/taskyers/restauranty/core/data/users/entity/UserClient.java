package pl.taskyers.restauranty.core.data.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.taskyers.restauranty.core.data.reservation.entity.Reservation;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_client")
@Data
@Builder
public class UserClient extends UserBase {
    
    private static final long serialVersionUID = -3913954803481656731L;
    
    @OneToMany(targetEntity = Reservation.class,mappedBy = "client",cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<Reservation> reservations = new HashSet<>();
    
    public UserClient() {
        super();
    }
    
    public UserClient(Set<Reservation> reservations) {
        super();
        this.reservations = reservations;
    }
    
    public UserClient(Long id, Role role, String username, String password, String email, boolean enabled, Set<Reservation> reservations) {
        super(id, role, username, password, email, enabled);
        this.reservations = reservations;
    }
    
}
