package pl.taskyers.restauranty.web.reviews.dto;

import lombok.Value;

@Value
public class RestaurantReviewDTO {
    
    long id;
    
    String content;
    
    int rate;
    
}
