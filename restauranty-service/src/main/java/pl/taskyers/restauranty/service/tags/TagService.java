package pl.taskyers.restauranty.service.tags;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;

import java.util.Set;

public interface TagService {
    
    /**
     * Get all tags by value from database and if there are not present - save
     *
     * @param tags {@link Set} of tags as strings
     * @return {@link Set} of {@link Tag} from database
     * @since 1.0.0
     */
    Set<Tag> saveAllAndGet(@NonNull final Set<String> tags);
    
}
