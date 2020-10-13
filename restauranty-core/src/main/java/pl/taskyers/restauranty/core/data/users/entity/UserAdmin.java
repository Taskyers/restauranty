package pl.taskyers.restauranty.core.data.users.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_admin")
@Data
@Builder
public class UserAdmin extends UserBase {
    
    private static final long serialVersionUID = -8167348917868909881L;
    
    public UserAdmin() {
        super();
    }
    
    public UserAdmin(Long id, Role role, String username, String password, String email) {
        super(id, role, username, password, email);
    }
    
}
