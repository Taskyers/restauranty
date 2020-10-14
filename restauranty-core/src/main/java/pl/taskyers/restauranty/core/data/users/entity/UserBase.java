package pl.taskyers.restauranty.core.data.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Base class for user types:
 * <ul>
 *     <li>{@link UserAdmin}</li>
 *     <li>{@link UserClient}</li>
 *     <li>{@link UserRestaurant}</li>
 * </ul>
 * Table <i>user</i> will be created and children tables will store reference. <br>
 * Should be used only when <i>main</i> user class is needed.
 */
@Entity
@Table(name = "user_base")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserBase implements Serializable {
    
    private static final long serialVersionUID = 1966232829588932170L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
}
