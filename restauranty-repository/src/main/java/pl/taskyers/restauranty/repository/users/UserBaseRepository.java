package pl.taskyers.restauranty.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserAdmin;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

import java.io.Serializable;

/**
 * Base repository for User classes:
 * <ul>
 *     <li>{@link UserAdmin}</li>
 *     <li>{@link UserClient}</li>
 *     <li>{@link UserRestaurant}</li>
 * </ul>
 * Explicit use in: {@link pl.taskyers.restauranty.auth}. Should not be use anywhere else. Instead use one of concrete implementations:
 * <ul>
 *     <li>{@link AdminUserRepository}</li>
 *     <li>{@link ClientUserRepository}</li>
 *     <li>{@link RestaurantUserRepository}</li>
 * </ul>
 *
 * @param <T>  class that extends {@link UserBase}
 * @param <ID> ID for entity, must extends {@link Serializable}
 * @author Jakub Sildatk
 * @since 1.0.0
 */
@Repository
public interface UserBaseRepository<T extends UserBase, ID extends Serializable> extends JpaRepository<T, ID> {
    
    T findByUsername(String username);
    
}