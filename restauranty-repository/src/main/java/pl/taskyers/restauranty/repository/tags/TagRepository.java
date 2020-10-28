package pl.taskyers.restauranty.repository.tags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Tag findByValueIgnoreCase(String value);
    
}
