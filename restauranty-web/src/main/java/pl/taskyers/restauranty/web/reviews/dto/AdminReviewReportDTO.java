package pl.taskyers.restauranty.web.reviews.dto;

import lombok.Value;

@Value
public class AdminReviewReportDTO {
    
    Long id;
    
    String user;
    
    String content;
    
    String restaurant;
    
}
