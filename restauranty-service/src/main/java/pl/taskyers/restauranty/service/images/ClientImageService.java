package pl.taskyers.restauranty.service.images;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;

public interface ClientImageService {
    
    String CLIENT_IMAGE_PREFIX = "/client/images";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    /**
     * Get main image for provided restaurant
     *
     * @param restaurant restaurant's name
     * @return {@link RestaurantImage}
     * @throws RestaurantNotFoundException if restaurant was not found
     * @throws ImageNotFoundException      if main image for provided restaurant does not exist
     * @since 1.0.0
     */
    RestaurantImage getMainImageForRestaurant(@NonNull final String restaurant) throws RestaurantNotFoundException, ImageNotFoundException;
    
}
