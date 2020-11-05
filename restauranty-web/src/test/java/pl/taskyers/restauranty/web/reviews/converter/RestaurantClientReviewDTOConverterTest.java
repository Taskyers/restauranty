package pl.taskyers.restauranty.web.reviews.converter;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.enums.ReviewRate;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.service.auth.AuthProvider;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantClientReviewDTO;

import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestaurantClientReviewDTOConverterTest {
    
    private AuthProvider authProvider;
    
    private RestaurantClientReviewDTOConverter restaurantClientReviewDTOConverter;
    
    @BeforeEach
    public void setUp() {
        authProvider = mock(AuthProvider.class);
        restaurantClientReviewDTOConverter = new RestaurantClientReviewDTOConverter(authProvider);
    }
    
    @Test
    public void testConvertingReviewstoDTO() {
        // given
        final UserBase user = new UserClient();
        user.setUsername("test");
        final Review review1 = Review.builder()
                .id(1L)
                .content("review1")
                .rate(ReviewRate.TWO)
                .user(user)
                .build();
        final Review review2 = Review.builder()
                .id(2L)
                .content("review2")
                .rate(ReviewRate.FIVE)
                .user(user)
                .build();
        final Review review3 = Review.builder()
                .id(3L)
                .content("review3")
                .rate(ReviewRate.FOUR)
                .user(user)
                .build();
        final List<Review> reviews = Lists.newArrayList(review1, review2, review3);
        when(authProvider.getUserLogin()).thenReturn("test");
        
        // when
        final Set<RestaurantClientReviewDTO> result = restaurantClientReviewDTOConverter.convertToResponseDTO(reviews);
        
        // then
        assertThat(result, iterableWithSize(3));
    }
    
}