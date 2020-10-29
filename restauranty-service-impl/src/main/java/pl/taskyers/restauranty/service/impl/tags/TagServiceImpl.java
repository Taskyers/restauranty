package pl.taskyers.restauranty.service.impl.tags;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;
import pl.taskyers.restauranty.repository.tags.TagRepository;
import pl.taskyers.restauranty.service.tags.TagService;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    
    private final TagRepository tagRepository;
    
    @Override
    public Set<Tag> saveAllAndGet(@NonNull Set<String> tags) {
        final Set<Tag> result = Sets.newHashSetWithExpectedSize(tags.size());
        for ( String tag : tags ) {
            final Tag byValue = tagRepository.findByValueIgnoreCase(tag);
            if ( byValue != null ) {
                result.add(byValue);
                log.debug("Adding already existing tag: {}", tag);
            } else {
                final Tag toSave = new Tag();
                toSave.setValue(tag.toLowerCase());
                result.add(tagRepository.save(toSave));
            }
        }
        return result;
    }
    
}
