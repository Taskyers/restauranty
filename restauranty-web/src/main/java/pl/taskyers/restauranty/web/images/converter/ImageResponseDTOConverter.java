package pl.taskyers.restauranty.web.images.converter;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.web.images.dto.ImageResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ImageResponseDTOConverter {
    
    public List<ImageResponseDTO> convertToDTO(Set<RestaurantImage> images) {
        final List<ImageResponseDTO> result = new ArrayList<>(images.size());
        for ( RestaurantImage restaurantImage : images ) {
            result.add(new ImageResponseDTO(restaurantImage.getName(), restaurantImage.getType(), restaurantImage.getSize()));
        }
        return result;
    }
    
}
