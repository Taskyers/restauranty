package pl.taskyers.restauranty.service.reviews;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;

import java.util.List;

public interface RestaurantReviewService {
    
    String PREFIX = "/restaurant/reviews";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    String REPORT_REVIEW = "/report";
    
    /**
     * Get all {@link Review} for restaurant
     *
     * @param restaurant restaurant's name
     * @return list of {@link Review}
     * @since 1.0.0
     */
    List<Review> getReviewsForRestaurant(@NonNull final String restaurant);
    
    ReviewReport reportReview(@NonNull final Long id, @NonNull final String restaurantName);
    
}
