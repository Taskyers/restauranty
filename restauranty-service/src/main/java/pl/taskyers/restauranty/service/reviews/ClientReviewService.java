package pl.taskyers.restauranty.service.reviews;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.ReviewNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.ReviewRateNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;

public interface ClientReviewService {
    
    String PREFIX = "/client/reviews";
    
    String BY_ID = "/{id}";
    
    String BY_RESTAURANT = "/{restaurant}";
    
    /**
     * Add review for restaurant
     *
     * @param restaurantName restaurant that is reviewed
     * @param content        review's content
     * @param rate           review's rate
     * @return saved {@link Review}
     * @throws ValidationException         if review's content is empty
     * @throws RestaurantNotFoundException if restaurant was not found
     * @throws ReviewRateNotFoundException if review rate was not found
     * @since 1.0.0
     */
    Review addReview(@NonNull final String restaurantName, @NonNull final String content, final int rate)
            throws ValidationException, RestaurantNotFoundException, ReviewRateNotFoundException;
    
    /**
     * Update review
     *
     * @param id      review's id
     * @param content review's content
     * @param rate    review's rate
     * @return updated {@link Review}
     * @throws ValidationException         if review's content is empty
     * @throws ReviewNotFoundException     if review was not found
     * @throws ReviewRateNotFoundException if review rate was not found
     * @since 1.0.0
     */
    Review updateReview(@NonNull final Long id, @NonNull final String content, final int rate)
            throws ValidationException, ReviewNotFoundException, ReviewRateNotFoundException;
    
    /**
     * Delete user's review by id
     *
     * @param id review's id
     * @throws ForbiddenException      if requested review does not belong for logged user
     * @throws ReviewNotFoundException if review was not found
     * @since 1.0.0
     */
    void deleteReview(@NonNull final Long id) throws ForbiddenException, ReviewNotFoundException;
    
}
