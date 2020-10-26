package pl.taskyers.restauranty.service.impl.reviews;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.ReviewNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.ReviewRateNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.enums.ReviewRate;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.service.impl.reviews.validator.ClientReviewValidator;
import pl.taskyers.restauranty.service.reviews.ClientReviewService;

@Service
@RequiredArgsConstructor
public class ClientReviewServiceImpl implements ClientReviewService {
    
    private final ReviewRepository reviewRepository;
    
    private final RestaurantRepository restaurantRepository;
    
    private final AuthProvider authProvider;
    
    @Override
    public Review addReview(@NonNull String restaurantName, @NonNull String content, int rate) {
        final Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        MessageProvider.getMessage(MessageCode.Restaurant.RESTAURANT_NOT_FOUND, "name", restaurantName)));
        validate(content);
        
        final UserBase user = authProvider.getUserEntity();
        final Review toSave = Review.builder()
                .user(user)
                .restaurant(restaurant)
                .content(content)
                .rate(ReviewRate.getByIntValue(rate))
                .build();
        return reviewRepository.save(toSave);
    }
    
    @Override
    public Review updateReview(@NonNull Long id, @NonNull String content, int rate)
            throws ValidationException, ReviewNotFoundException, ReviewRateNotFoundException {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(MessageProvider.getMessage(MessageCode.Review.REVIEW_NOT_FOUND, "id", id)));
        validate(content);
        
        review.setContent(content);
        review.setRate(ReviewRate.getByIntValue(rate));
        return reviewRepository.save(review);
    }
    
    @Override
    public void deleteReview(@NonNull Long id) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(MessageProvider.getMessage(MessageCode.Review.REVIEW_NOT_FOUND, "id", id)));
        if ( !review.getUser()
                .getUsername()
                .equals(authProvider.getUserLogin()) ) {
            throw new ForbiddenException(MessageCode.Review.REVIEW_NOT_YOURS);
        }
        reviewRepository.deleteById(id);
    }
    
    private void validate(String content) {
        final ValidationMessageContainer validationMessageContainer = ClientReviewValidator.validate(content);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
    }
    
}
