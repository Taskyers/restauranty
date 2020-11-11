package pl.taskyers.restauranty.web.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.reviews.AdminReviewService;
import pl.taskyers.restauranty.web.reviews.converter.AdminReviewReportDTOConverter;
import pl.taskyers.restauranty.web.reviews.dto.AdminReviewReportDTO;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(AdminReviewService.PREFIX)
public class AdminReviewRestController {
    
    private final AdminReviewService adminReviewService;
    
    @GetMapping
    public ResponseEntity<Set<AdminReviewReportDTO>> getAllReports() {
        final List<ReviewReport> reviewReports = adminReviewService.getReportedReviews();
        return ResponseEntity.ok(AdminReviewReportDTOConverter.convertReports(reviewReports));
    }
    
    @PutMapping(AdminReviewService.ACCEPT)
    public ResponseEntity<ResponseMessage<String>> acceptReviewReport(@PathVariable final Long id) {
        adminReviewService.removeReview(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.Report.REPORT_ACCEPTED, MessageType.SUCCESS));
    }
    
    @PutMapping(AdminReviewService.REJECT)
    public ResponseEntity<ResponseMessage<String>> rejectReviewReport(@PathVariable final Long id) {
        adminReviewService.rejectRequest(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.Report.REPORT_REJECTED, MessageType.SUCCESS));
    }
    
}
