package pl.taskyers.restauranty.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Role findByRole(RoleType role);
    
}
