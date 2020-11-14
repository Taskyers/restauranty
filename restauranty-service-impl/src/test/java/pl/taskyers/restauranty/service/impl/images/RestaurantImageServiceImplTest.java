package pl.taskyers.restauranty.service.impl.images;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.images.ImageStorageService;
import pl.taskyers.restauranty.service.images.RestaurantImageService;
import pl.taskyers.restauranty.service.impl.images.validator.RestaurantImageValidator;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class RestaurantImageServiceImplTest {
    
    private RestaurantRepository restaurantRepository;
    
    private RestaurantImageRepository restaurantImageRepository;
    
    private ImageStorageService imageStorageService;
    
    private RestaurantImageValidator restaurantImageValidator;
    
    private RestaurantImageService restaurantImageService;
    
    @BeforeEach
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        restaurantImageRepository = mock(RestaurantImageRepository.class);
        imageStorageService = mock(ImageStorageService.class);
        restaurantImageValidator = mock(RestaurantImageValidator.class);
        restaurantImageService =
                new RestaurantImageServiceImpl(restaurantRepository, restaurantImageRepository, imageStorageService, restaurantImageValidator);
    }
    
    @Test
    public void testGettingImagesForNotExistingRestaurant() {
        // given
        final String name = "asdf";
        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> restaurantImageService.getAllForRestaurant(name));
        
        // then
        assertThat(result.getMessage(), is(String.format("Restaurant with name %s was not found", name)));
    }
    
    @Test
    public void testGettingImagesForExistingRestaurant() {
        // given
        final String name = "asdf";
        final Set<RestaurantImage> images = Sets.newHashSet(new RestaurantImage(), new RestaurantImage(), new RestaurantImage());
        final Restaurant restaurant = new Restaurant();
        restaurant.setImages(images);
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        
        // when
        final Set<RestaurantImage> result = restaurantImageService.getAllForRestaurant(name);
        
        // then
        assertThat(result, is(images));
        
    }
    
    @Test
    public void testSavingInvalidImage() {
        // given
        final String name = "asdf";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        validationMessageContainer.addError("test", "test");
        when(restaurantImageValidator.validate(image)).thenReturn(validationMessageContainer);
        
        // when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> restaurantImageService.saveImage(image, name, false));
        
        // then
        assertThat(result.getMessages(), is(validationMessageContainer.getErrors()));
    }
    
    @Test
    public void testSavingValidImage() {
        // given
        final String name = "asdf.png";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        final Restaurant restaurant = new Restaurant();
        when(restaurantImageValidator.validate(image)).thenReturn(new ValidationMessageContainer());
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        when(restaurantImageRepository.save(any(RestaurantImage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final RestaurantImage result = restaurantImageService.saveImage(image, name, false);
        
        // then
        assertThat(result.getName(), is(name));
        assertThat(result.getType(), is(image.getContentType()));
        assertThat(result.isMain(), is(false));
        verify(imageStorageService).store(image);
    }
    
    @Test
    public void testSavingNewMainImage() {
        // given
        final String name = "asdf.png";
        final MultipartFile image = new MockMultipartFile(name, name, "image/png", new byte[]{});
        final Restaurant restaurant = new Restaurant();
        final RestaurantImage mainImage = RestaurantImage.builder()
                .main(true)
                .build();
        when(restaurantImageValidator.validate(image)).thenReturn(new ValidationMessageContainer());
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        when(restaurantImageRepository.findByRestaurantAndMain(restaurant, true)).thenReturn(Optional.of(mainImage));
        when(restaurantImageRepository.save(any(RestaurantImage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final RestaurantImage result = restaurantImageService.saveImage(image, name, true);
        
        // then
        assertThat(result.getName(), is(name));
        assertThat(result.getType(), is(image.getContentType()));
        assertThat(result.isMain(), is(true));
        verify(restaurantImageRepository).save(mainImage);
        verify(imageStorageService).store(image);
    }
    
    @Test
    public void testGettingNotExistingImage() {
        // given
        final String name = "asf.png";
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final ImageNotFoundException result = assertThrows(ImageNotFoundException.class, () -> restaurantImageService.getImage(name));
        
        // then
        assertThat(result.getMessage(), is(String.format("Image with %s name was not found", name)));
    }
    
    @Test
    public void testChangingMainImage() {
        // given
        final String name = "test.png";
        final Restaurant restaurant = Restaurant.builder()
                .name("test")
                .build();
        final RestaurantImage restaurantImage = RestaurantImage.builder()
                .name(name)
                .main(false)
                .restaurant(restaurant)
                .build();
        final RestaurantImage mainImage = RestaurantImage.builder()
                .main(true)
                .build();
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.of(restaurantImage));
        when(restaurantImageRepository.findByRestaurantAndMain(restaurant, true)).thenReturn(Optional.of(mainImage));
        when(restaurantImageRepository.save(any(RestaurantImage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final RestaurantImage result = restaurantImageService.setMainImage(name);
        
        // then
        assertThat(result.isMain(), is(true));
        verify(restaurantImageRepository).save(mainImage);
    }
    
    @Test
    public void testGettingExistingImage() {
        // given
        final String name = "asf.png";
        final RestaurantImage restaurantImage = new RestaurantImage();
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.of(restaurantImage));
        
        // when
        final RestaurantImage result = restaurantImageService.getImage(name);
        
        // then
        assertThat(result, is(restaurantImage));
    }
    
    @Test
    public void testRemovingNotExistingImage() {
        // given
        final String name = "asf.png";
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final ImageNotFoundException result = assertThrows(ImageNotFoundException.class, () -> restaurantImageService.deleteImage(name));
        
        // then
        assertThat(result.getMessage(), is(String.format("Image with %s name was not found", name)));
        
    }
    
    @Test
    public void testRemovingExistingImage() {
        // given
        final String name = "asf.png";
        final RestaurantImage restaurantImage = new RestaurantImage();
        restaurantImage.setId(2L);
        restaurantImage.setName(name);
        when(restaurantImageRepository.findByName(name)).thenReturn(Optional.of(restaurantImage));
        
        // when
        restaurantImageService.deleteImage(name);
        
        // then
        verify(restaurantImageRepository).deleteById(restaurantImage.getId());
        verify(imageStorageService).delete(restaurantImage.getName());
    }
    
}