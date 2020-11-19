package pl.taskyers.restauranty.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;

@Repository
public interface MenuRepository extends JpaRepository<SingleMenuDish, Long> {
}
