package pl.taskyers.restauranty.web.reviews.converter;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantReviewDTO;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RestaurantReviewDTOConverter {
    
    public List<RestaurantReviewDTO> convertToDTOList(List<Review> reviews) {
        final List<RestaurantReviewDTO> result = new ArrayList<>(reviews.size());
        for ( Review review : reviews ) {
            result.add(new RestaurantReviewDTO(review.getId(), review.getContent(), review.getRate()
                    .getValue()));
        }
        return result;
    }
    
}
