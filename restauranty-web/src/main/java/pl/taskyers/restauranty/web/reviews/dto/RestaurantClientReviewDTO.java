package pl.taskyers.restauranty.web.reviews.dto;

import lombok.Value;

@Value
public class RestaurantClientReviewDTO {
    
    long id;
    
    String user;
    
    String content;
    
    int rate;
    
    boolean isOwner;
    
}
