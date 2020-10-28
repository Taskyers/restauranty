package pl.taskyers.restauranty.service.impl.tags;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.restaurants.tags.entity.Tag;
import pl.taskyers.restauranty.repository.tags.TagRepository;

import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    
    private TagRepository tagRepository;
    
    private TagServiceImpl tagService;
    
    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagServiceImpl(tagRepository);
    }
    
    @Test
    public void testSavingTags() {
        // given
        final String tag1 = "test1";
        final String tag2 = "test2";
        final Tag tagFromDatabase = new Tag(1L, tag1);
        final Set<String> tags = Sets.newHashSet(tag1, tag2);
        when(tagRepository.findByValueIgnoreCase(tag1)).thenReturn(tagFromDatabase);
        when(tagRepository.findByValueIgnoreCase(tag2)).thenReturn(null);
        
        // when
        final Set<Tag> result = tagService.saveAllAndGet(tags);
        
        // then
        verify(tagRepository).save(any());
        assertThat(result, iterableWithSize(2));
        assertThat(result, hasItem(tagFromDatabase));
    }
    
}