package pl.taskyers.restauranty.service.impl.images;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.images.ImageStorageService;
import pl.taskyers.restauranty.service.images.RestaurantImageService;
import pl.taskyers.restauranty.service.impl.images.validator.RestaurantImageValidator;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantImageServiceImpl implements RestaurantImageService {
    
    private final RestaurantRepository restaurantRepository;
    
    private final RestaurantImageRepository restaurantImageRepository;
    
    private final ImageStorageService imageStorageService;
    
    private final RestaurantImageValidator restaurantImageValidator;
    
    @Override
    public Set<RestaurantImage> getAllForRestaurant(@NonNull String restaurantName) {
        return getRestaurant(restaurantName).getImages();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RestaurantImage saveImage(@NonNull final MultipartFile image, @NonNull final String restaurantName) {
        final ValidationMessageContainer validationMessageContainer = restaurantImageValidator.validate(image);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        final Restaurant restaurant = getRestaurant(restaurantName);
        final String path = imageStorageService.store(image);
        final RestaurantImage toSave = RestaurantImage.builder()
                .restaurant(restaurant)
                .name(image.getOriginalFilename())
                .type(image.getContentType())
                .path(path)
                .size(image.getSize())
                .build();
        
        return restaurantImageRepository.save(toSave);
    }
    
    @Override
    public RestaurantImage getImage(@NonNull final String name) {
        return restaurantImageRepository.findByName(name)
                .orElseThrow(() -> new ImageNotFoundException(MessageProvider.getMessage(MessageCode.Images.IMAGE_NOT_FOUND, name)));
    }
    
    @Override
    public void deleteImage(@NonNull final String name) {
        final RestaurantImage image = getImage(name);
        restaurantImageRepository.deleteById(image.getId());
        imageStorageService.delete(name);
    }
    
    private Restaurant getRestaurant(String name) {
        return restaurantRepository.findByName(name)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_WITH_FIELD_NOT_FOUND, "name", name)));
    }
    
}
