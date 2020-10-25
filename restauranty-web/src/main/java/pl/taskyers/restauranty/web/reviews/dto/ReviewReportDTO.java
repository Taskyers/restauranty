package pl.taskyers.restauranty.web.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReportDTO {
    
    private Long reviewId;
    
    private String restaurant;
    
}
