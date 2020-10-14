package pl.taskyers.restauranty.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserAdmin;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base repository for User classes:
 * <ul>
 *     <li>{@link UserBase}</li>
 *     <li>{@link UserAdmin}</li>
 *     <li>{@link UserClient}</li>
 *     <li>{@link UserRestaurant}</li>
 * </ul>
 * Should not be use injected anywhere. Instead use one of concrete implementations:
 * <ul>
 *     <li>{@link UserRepository}</li>
 *     <li>{@link UserAdminRepository}</li>
 *     <li>{@link UserClientRepository}</li>
 *     <li>{@link UserRestaurantRepository}</li>
 * </ul>
 *
 * @param <T>  class that extends {@link UserBase}
 * @param <ID> ID for entity, must extends {@link Serializable}
 * @author Jakub Sildatk
 * @since 1.0.0
 */
@NoRepositoryBean
interface UserBaseRepository<T extends UserBase, ID extends Serializable> extends JpaRepository<T, ID> {
    
    Optional<T> findByUsername(String username);
    
    Optional<T> findByEmail(String email);
    
}