package pl.taskyers.restauranty.web.reviews.dto;

import lombok.Value;

@Value
public class UpdateReviewDTO {
    
    String content;
    
    int rate;
    
}
