package pl.taskyers.restauranty.web.reviews.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.service.reviews.RestaurantReviewService;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantReviewDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantReviewDTOConverter {
    
    private final RestaurantReviewService restaurantReviewService;
    
    public List<RestaurantReviewDTO> convertToDTOList(List<Review> reviews) {
        final List<RestaurantReviewDTO> result = new ArrayList<>(reviews.size());
        for ( Review review : reviews ) {
            result.add(new RestaurantReviewDTO(review.getId(), review.getContent(), review.getRate()
                    .getValue(), review.getUser()
                    .getUsername(), restaurantReviewService.isAlreadyReported(review)));
        }
        return result;
    }
    
}
