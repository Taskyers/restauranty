package pl.taskyers.restauranty.service.impl.reviews;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.data.reviews.ReviewNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.ReviewRateNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.enums.ReviewRate;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.core.error.exceptions.ForbiddenException;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.repository.restaurants.RestaurantRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.mockito.Mockito.*;

public class ClientReviewServiceImplTest {
    
    private ReviewRepository reviewRepository;
    
    private RestaurantRepository restaurantRepository;
    
    private AuthProvider authProvider;
    
    private ClientReviewServiceImpl clientReviewService;
    
    @BeforeEach
    public void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        restaurantRepository = mock(RestaurantRepository.class);
        authProvider = mock(AuthProvider.class);
        clientReviewService = new ClientReviewServiceImpl(reviewRepository, restaurantRepository, authProvider);
    }
    
    @Test
    public void testAddingReviewWhenRestaurantIsNotFound() {
        // given
        final String name = "asdf";
        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());
        
        // when
        final RestaurantNotFoundException result =
                assertThrows(RestaurantNotFoundException.class, () -> clientReviewService.addReview(name, "asdf", 1));
        
        // then
        assertThat(result.getMessage(), is(String.format("Restaurant with name %s was not found", name)));
    }
    
    @Test
    public void testAddingReviewWithBlankContent() {
        // given
        final String content = "   ";
        when(restaurantRepository.findByName(anyString())).thenReturn(Optional.of(new Restaurant()));
        
        // when
        final ValidationException result =
                assertThrows(ValidationException.class, () -> clientReviewService.addReview("asd", content, 1));
        
        // then
        assertThat(result.getMessages(), iterableWithSize(1));
    }
    
    @Test
    public void testAddingReviewWithInvalidRate() {
        // given
        final int rate = 6;
        when(restaurantRepository.findByName(anyString())).thenReturn(Optional.of(new Restaurant()));
        
        // when
        final ReviewRateNotFoundException result =
                assertThrows(ReviewRateNotFoundException.class, () -> clientReviewService.addReview("asdf", "asdf", rate));
        
        // then
        assertThat(result.getMessage(), is("Rate with value " + rate + " was not found"));
    }
    
    @Test
    public void testAddingValidReview() {
        // given
        final UserBase user = new UserClient();
        final Restaurant restaurant = new Restaurant();
        final String content = "content";
        final int rate = 1;
        when(restaurantRepository.findByName(anyString())).thenReturn(Optional.of(restaurant));
        when(authProvider.getUserEntity()).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final Review result = clientReviewService.addReview("asdf", content, rate);
        
        // then
        assertThat(result, notNullValue());
        assertThat(result.getUser(), is(user));
        assertThat(result.getRestaurant(), is(restaurant));
        assertThat(result.getContent(), is(content));
        assertThat(result.getRate(), is(ReviewRate.getByIntValue(rate)));
    }
    
    @Test
    public void testUpdatingNotExistingReview() {
        // given
        final long id = 1L;
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());
        
        // when
        final ReviewNotFoundException result = assertThrows(ReviewNotFoundException.class, () -> clientReviewService.updateReview(id, "asd", 2));
        
        // then
        assertThat(result.getMessage(), is("Review with id " + id + " was not found"));
    }
    
    @Test
    public void testUpdatingExistingReview() {
        // given
        final long id = 1L;
        final UserBase user = new UserClient();
        final Restaurant restaurant = new Restaurant();
        final String content = "content";
        final int rate = 2;
        final Review review = new Review(id, user, restaurant, "asd", ReviewRate.getByIntValue(1));
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final Review result = clientReviewService.updateReview(id, content, rate);
        
        // then
        assertThat(result.getId(), is(id));
        assertThat(result.getUser(), is(user));
        assertThat(result.getRestaurant(), is(restaurant));
        assertThat(result.getContent(), is(content));
        assertThat(result.getRate(), is(ReviewRate.getByIntValue(rate)));
    }
    
    @Test
    public void testUpdatingExistingReviewWithSameData() {
        // given
        final long id = 1L;
        final UserBase user = new UserClient();
        final Restaurant restaurant = new Restaurant();
        final String content = "content";
        final int rate = 2;
        final Review review = new Review(id, user, restaurant, content, ReviewRate.getByIntValue(rate));
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        
        // when
        final Review result = clientReviewService.updateReview(id, content, rate);
        
        // then
        assertThat(result.getId(), is(id));
        assertThat(result.getUser(), is(user));
        assertThat(result.getRestaurant(), is(restaurant));
        assertThat(result.getContent(), is(content));
        assertThat(result.getRate(), is(ReviewRate.getByIntValue(rate)));
    }
    
    @Test
    public void testDeletingNotExistingReview() {
        // given
        final long id = 1L;
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());
        
        // when
        final ReviewNotFoundException result = assertThrows(ReviewNotFoundException.class, () -> clientReviewService.deleteReview(id));
        
        // then
        assertThat(result.getMessage(), is("Review with id " + id + " was not found"));
    }
    
    @Test
    public void testDeletingSomeoneOtherReview() {
        // given
        final long id = 1L;
        final UserBase user = new UserClient();
        user.setUsername("test");
        final Review review = new Review();
        review.setUser(user);
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(authProvider.getUserLogin()).thenReturn("asdf");
        
        // when
        final ForbiddenException result = assertThrows(ForbiddenException.class, () -> clientReviewService.deleteReview(id));
        
        // then
        assertThat(result.getMessage(), is("Review is not yours"));
    }
    
    @Test
    public void testDeletingValidReview() {
        // given
        final long id = 1L;
        final String login = "test";
        final Review review = new Review();
        final UserClient user = new UserClient();
        user.setUsername(login);
        review.setUser(user);
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(authProvider.getUserLogin()).thenReturn(login);
        
        // when
        clientReviewService.deleteReview(id);
        
        // then
        verify(reviewRepository).deleteById(id);
    }
    
}