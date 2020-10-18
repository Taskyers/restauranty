package pl.taskyers.restauranty.service.impl.images.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestaurantImageValidatorTest {
    
    private RestaurantImageRepository restaurantImageRepository;
    
    private RestaurantImageValidator restaurantImageValidator;
    
    @BeforeEach
    public void setUp() {
        restaurantImageRepository = mock(RestaurantImageRepository.class);
        restaurantImageValidator = new RestaurantImageValidator(restaurantImageRepository);
    }
    
    @Test
    public void testValidatingValidImage() {
        // given
        final String name = "asdf.png";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final ValidationMessageContainer result = restaurantImageValidator.validate(image);
        
        // then
        assertThat(result.getErrors(), iterableWithSize(0));
    }
    
    @Test
    public void testValidatingWithDuplicatedName() {
        // given
        final String name = "asdf.png";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.of(new RestaurantImage()));
        
        // when
        final ValidationMessageContainer result = restaurantImageValidator.validate(image);
        
        // then
        assertThat(result.getErrors(), iterableWithSize(1));
    }
    
    @Test
    public void testValidatingWithInvalidExtension() {
        // given
        final String name = "asdf.dasg";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final ValidationMessageContainer result = restaurantImageValidator.validate(image);
        
        // then
        assertThat(result.getErrors(), iterableWithSize(1));
    }
    
}