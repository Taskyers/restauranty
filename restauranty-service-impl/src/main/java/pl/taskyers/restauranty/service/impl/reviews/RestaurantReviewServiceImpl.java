package pl.taskyers.restauranty.service.impl.reviews;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.ReviewNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.ValidationMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewReportRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;
import pl.taskyers.restauranty.service.reviews.RestaurantReviewService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantReviewServiceImpl implements RestaurantReviewService {
    
    private final ReviewRepository reviewRepository;
    
    private final RestaurantRepository restaurantRepository;
    
    private final ReviewReportRepository reviewReportRepository;
    
    @Override
    public boolean isAlreadyReported(@NonNull Review review) {
        return reviewReportRepository.findByReview(review)
                .isPresent();
    }
    
    @Override
    public List<Review> getReviewsForRestaurant(@NonNull String restaurant) {
        return reviewRepository.findAllByRestaurantName(restaurant);
    }
    
    @Override
    public ReviewReport reportReview(@NonNull Long id, @NonNull String restaurantName) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(MessageProvider.getMessage(MessageCode.Review.REVIEW_NOT_FOUND, "id", id)));
        final Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "name", restaurantName)));
        if ( reviewReportRepository.findByReview(review)
                .isPresent() ) {
            throw new ValidationException(
                    Collections.singletonList(new ValidationMessage(MessageCode.Review.Report.REPORT_EXISTS, MessageType.ERROR, "review")));
        }
        
        final ReviewReport toSave = ReviewReport.builder()
                .review(review)
                .restaurant(restaurant)
                .build();
        return reviewReportRepository.save(toSave);
    }
    
}
