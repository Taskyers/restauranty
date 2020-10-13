package pl.taskyers.restauranty.repository.users;

import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.UserAdmin;

@Repository
public interface AdminUserRepository extends UserBaseRepository<UserAdmin, Long> {
}
