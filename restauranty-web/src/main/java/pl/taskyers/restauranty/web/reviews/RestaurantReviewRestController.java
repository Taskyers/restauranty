package pl.taskyers.restauranty.web.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.reviews.RestaurantReviewService;
import pl.taskyers.restauranty.web.reviews.converter.RestaurantReviewDTOConverter;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantReviewDTO;
import pl.taskyers.restauranty.web.reviews.dto.ReviewReportDTO;
import pl.taskyers.restauranty.web.util.UriUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestaurantReviewService.PREFIX)
public class RestaurantReviewRestController {
    
    private final RestaurantReviewService restaurantReviewService;
    
    @GetMapping(RestaurantReviewService.BY_RESTAURANT)
    public ResponseEntity<List<RestaurantReviewDTO>> getAllReviewsForRestaurant(@PathVariable final String restaurant) {
        return ResponseEntity.ok(RestaurantReviewDTOConverter.convertToDTOList(restaurantReviewService.getReviewsForRestaurant(restaurant)));
    }
    
    @PostMapping(RestaurantReviewService.REPORT_REVIEW)
    public ResponseEntity<ResponseMessage<String>> reportReview(@RequestBody final ReviewReportDTO reviewReportDTO) {
        final ReviewReport savedReport = restaurantReviewService.reportReview(reviewReportDTO.getReviewId(), reviewReportDTO.getRestaurant());
        return ResponseEntity.created(UriUtils.createURIFromId(savedReport.getId()))
                .body(new ResponseMessage<>(MessageCode.Review.Report.REPORT_SENT, MessageType.SUCCESS));
    }
    
}
