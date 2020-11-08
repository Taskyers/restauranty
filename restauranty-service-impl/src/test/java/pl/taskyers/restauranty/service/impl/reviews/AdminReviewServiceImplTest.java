package pl.taskyers.restauranty.service.impl.reviews;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.ReviewReportNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.email.service.EmailService;
import pl.taskyers.restauranty.email.service.enums.EmailType;
import pl.taskyers.restauranty.repository.reviews.ReviewReportRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

public class AdminReviewServiceImplTest {
    
    private ReviewReportRepository reviewReportRepository;
    
    private ReviewRepository reviewRepository;
    
    private EmailService emailService;
    
    private AdminReviewServiceImpl adminReviewService;
    
    @BeforeEach
    public void setUp() {
        reviewReportRepository = mock(ReviewReportRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        emailService = mock(EmailService.class);
        adminReviewService = new AdminReviewServiceImpl(reviewReportRepository, reviewRepository, emailService);
    }
    
    @Test
    public void testRemovingNotExistingReviewReport() {
        // given
        final long id = 1L;
        when(reviewReportRepository.findById(id)).thenReturn(Optional.empty());
        
        // when
        final ReviewReportNotFoundException result = assertThrows(ReviewReportNotFoundException.class, () -> adminReviewService.removeReview(id));
        
        // then
        assertThat(result.getMessage(), is("Review report with id " + id + " was not found"));
    }
    
    @Test
    public void testRemovingReviewReport() {
        // given
        final long id = 1L;
        final UserBase user = new UserClient();
        user.setUsername("test");
        final UserRestaurant userRestaurant = new UserRestaurant();
        userRestaurant.setEmail("test@email.com");
        final Review review = Review.builder()
                .id(id)
                .user(user)
                .build();
        final Restaurant restaurant = Restaurant.builder()
                .owner(userRestaurant)
                .build();
        final ReviewReport reviewReport = new ReviewReport(id, review, restaurant);
        when(reviewReportRepository.findById(id)).thenReturn(Optional.of(reviewReport));
        
        // then
        adminReviewService.removeReview(id);
        
        // when
        verify(reviewRepository).deleteById(id);
        verify(reviewReportRepository).deleteById(id);
        verify(emailService).sendEmailToSingleAddressee(userRestaurant.getEmail(), EmailType.REVIEW_REPORT_POSITIVE, user.getUsername());
    }
    
    @Test
    public void testRejectingReviewReport() {
        // given
        final long id = 1L;
        final UserBase user = new UserClient();
        user.setUsername("test");
        final UserRestaurant userRestaurant = new UserRestaurant();
        userRestaurant.setEmail("test@email.com");
        final Review review = Review.builder()
                .id(id)
                .user(user)
                .build();
        final Restaurant restaurant = Restaurant.builder()
                .owner(userRestaurant)
                .build();
        final ReviewReport reviewReport = new ReviewReport(id, review, restaurant);
        when(reviewReportRepository.findById(id)).thenReturn(Optional.of(reviewReport));
        
        // then
        adminReviewService.rejectRequest(id);
        
        // when
        verify(reviewRepository, never()).deleteById(id);
        verify(reviewReportRepository).deleteById(id);
        verify(emailService).sendEmailToSingleAddressee(userRestaurant.getEmail(), EmailType.REVIEW_REPORT_NEGATIVE, user.getUsername());
    }
    
}