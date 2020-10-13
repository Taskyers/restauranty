package pl.taskyers.restauranty.core.data.users.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_client")
@Data
@Builder
public class UserClient extends UserBase {
    
    private static final long serialVersionUID = -3913954803481656731L;
    
    public UserClient() {
        super();
    }
    
    public UserClient(Long id, Role role, String username, String password, String email) {
        super(id, role, username, password, email);
    }
    
}
