package pl.taskyers.restauranty.web.reviews.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantClientReviewDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RestaurantClientReviewDTOConverter {
    
    private final AuthProvider authProvider;
    
    public Set<RestaurantClientReviewDTO> convertToResponseDTO(final List<Review> reviews) {
        final Set<RestaurantClientReviewDTO> result = new HashSet<>();
        for ( Review review : reviews ) {
            result.add(convertReview(review));
        }
        return result;
    }
    
    public RestaurantClientReviewDTO convertReview(final Review review) {
        final String username = review.getUser()
                .getUsername();
        return new RestaurantClientReviewDTO(review.getId(), username, review.getContent(), review.getRate()
                .getValue(), authProvider.getUserLogin()
                .equals(username));
    }
    
}
