package pl.taskyers.restauranty.service.images;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;

import java.util.Set;

public interface RestaurantImageService {
    
    String RESTAURANT_IMAGE_PREFIX = "/restaurant/images";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    String BY_NAME = "/{name}";
    
    String GET_BY_NAME = "/get" + BY_NAME;
    
    /**
     * Get all images for restaurant
     *
     * @param restaurantName restaurant's name as string
     * @return {@link Set} of {@link RestaurantImage}
     * @throws RestaurantNotFoundException if provided restaurant was not found
     * @since 1.0.0
     */
    Set<RestaurantImage> getAllForRestaurant(@NonNull final String restaurantName) throws RestaurantNotFoundException;
    
    /**
     * Save new image for restaurant in database and store it in file system
     *
     * @param image          {@link MultipartFile}
     * @param restaurantName restaurant's name as string
     * @param isMain         flag for indicating if image is main for restaurant
     * @return saved {@link RestaurantImage}
     * @throws RestaurantNotFoundException if provided restaurant was not found
     * @throws ValidationException         if image's extension was invalid or image with provided name already exists
     * @see pl.taskyers.restauranty.service.impl.images.validator.RestaurantImageValidator
     * @see ImageStorageService
     * @since 1.0.0
     */
    RestaurantImage saveImage(@NonNull final MultipartFile image, @NonNull final String restaurantName, final boolean isMain)
            throws RestaurantNotFoundException, ValidationException;
    
    /**
     * Get image by name
     *
     * @param name image's name
     * @return {@link RestaurantImage}
     * @throws ImageNotFoundException if provided image was not found
     * @since 1.0.0
     */
    RestaurantImage getImage(@NonNull final String name) throws ImageNotFoundException;
    
    /**
     * Mark given image as main for restaurant
     *
     * @param name image's name
     * @return updated {@link RestaurantImage}
     * @throws ImageNotFoundException if provided image was not found
     * @since 1.0.0
     */
    RestaurantImage setMainImage(@NonNull final String name) throws ImageNotFoundException;
    
    /**
     * Delete image by name from database and from file system
     *
     * @param name image's name
     * @throws ImageNotFoundException if provided image was not found
     * @since 1.0.0
     */
    void deleteImage(@NonNull final String name) throws ImageNotFoundException;
    
}
