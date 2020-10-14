package pl.taskyers.restauranty.repository.users;

import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

@Repository
public interface UserRepository extends UserBaseRepository<UserBase, Long> {
}
