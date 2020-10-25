package pl.taskyers.restauranty.service.impl.reviews;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.ReviewNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewReportRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestaurantReviewServiceImplTest {
    
    private ReviewRepository reviewRepository;
    
    private RestaurantRepository restaurantRepository;
    
    private ReviewReportRepository reviewReportRepository;
    
    private RestaurantReviewServiceImpl restaurantReviewService;
    
    @BeforeEach
    public void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        reviewReportRepository = mock(ReviewReportRepository.class);
        restaurantReviewService = new RestaurantReviewServiceImpl(reviewRepository, restaurantRepository, reviewReportRepository);
    }
    
    @Test
    public void testReportingReviewWhenReviewIsNotFound() {
        // given
        final long id = 1L;
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());
        
        // when
        final ReviewNotFoundException result = assertThrows(ReviewNotFoundException.class, () -> restaurantReviewService.reportReview(id, "test"));
        
        // then
        assertThat(result.getMessage(), is("Review with id " + id + " was not found"));
    }
    
    @Test
    public void testReportingReviewWhenRestaurantIsNotFound() {
        // given
        final String name = "test";
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> restaurantReviewService.reportReview(1L, name));
        
        // then
        assertThat(result.getMessage(), is("Restaurant with name " + name + " was not found"));
    }
    
    @Test
    public void testReportingReviewWhenReportExists() {
        // given
        final String name = "test";
        final Review review = new Review();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(new Restaurant()));
        when(reviewReportRepository.findByReview(review)).thenReturn(Optional.of(new ReviewReport()));
        
        // when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> restaurantReviewService.reportReview(1L, name));
        
        // then
        assertThat(result.getMessages()
                .get(0)
                .getMessage(), is("Report for this review already exists"));
    }
    
    @Test
    public void testReportingValidReview() {
        // given
        final Review review = new Review();
        final Restaurant restaurant = new Restaurant();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(restaurantRepository.findByName(anyString())).thenReturn(Optional.of(restaurant));
        when(reviewReportRepository.findByReview(review)).thenReturn(Optional.empty());
        when(reviewReportRepository.save(any(ReviewReport.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final ReviewReport result = restaurantReviewService.reportReview(1L, "test");
        
        // then
        assertThat(result, notNullValue());
        assertThat(result.getReview(), is(review));
        assertThat(result.getRestaurant(), is(restaurant));
    }
    
}