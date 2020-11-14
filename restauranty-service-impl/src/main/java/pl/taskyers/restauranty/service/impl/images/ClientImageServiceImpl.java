package pl.taskyers.restauranty.service.impl.images;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.images.ImageNotFoundException;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.service.images.ClientImageService;

@Service
@RequiredArgsConstructor
public class ClientImageServiceImpl implements ClientImageService {
    
    private final RestaurantRepository restaurantRepository;
    
    private final RestaurantImageRepository restaurantImageRepository;
    
    @Override
    public RestaurantImage getMainImageForRestaurant(@NonNull String restaurant) throws RestaurantNotFoundException, ImageNotFoundException {
        final Restaurant restaurantFromDatabase = restaurantRepository.findByName(restaurant)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "name", restaurant)));
        return restaurantImageRepository.findByRestaurantAndMain(restaurantFromDatabase, true)
                .orElseThrow(() -> new ImageNotFoundException(MessageProvider.getMessage(MessageCode.Images.MAIN_IMAGE_NOT_FOUND, restaurant)));
    }
    
}
