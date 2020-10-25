package pl.taskyers.restauranty.web.reviews.dto;

import lombok.Value;

@Value
public class ClientReviewDTO {
    
    String restaurant;
    
    String content;
    
    int rate;
    
}
