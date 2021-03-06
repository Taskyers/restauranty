package pl.taskyers.restauranty.repository.users;

import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;

@Repository
public interface UserClientRepository extends UserBaseRepository<UserClient, Long> {
}
