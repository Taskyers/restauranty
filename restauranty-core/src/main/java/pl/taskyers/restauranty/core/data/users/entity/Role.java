package pl.taskyers.restauranty.core.data.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;

import javax.persistence.*;
import java.io.Serializable;

/*sd*/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    
    private static final long serialVersionUID = 7322566530140742722L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;
    
}
