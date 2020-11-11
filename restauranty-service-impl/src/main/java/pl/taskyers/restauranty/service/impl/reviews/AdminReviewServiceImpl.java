package pl.taskyers.restauranty.service.impl.reviews;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.reviews.ReviewReportNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.email.service.EmailService;
import pl.taskyers.restauranty.email.service.enums.EmailType;
import pl.taskyers.restauranty.repository.reviews.ReviewReportRepository;
import pl.taskyers.restauranty.repository.reviews.ReviewRepository;
import pl.taskyers.restauranty.service.reviews.AdminReviewService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminReviewServiceImpl implements AdminReviewService {
    
    private final ReviewReportRepository reviewReportRepository;
    
    private final ReviewRepository reviewRepository;
    
    private final EmailService emailService;
    
    @Override
    public List<ReviewReport> getReportedReviews() {
        return reviewReportRepository.findAll();
    }
    
    @Override
    public void removeReview(@NonNull Long id) {
        final ReviewReport reviewReport = getReviewReport(id);
        final Review review = reviewReport.getReview();
        final String restaurantEmail = reviewReport.getRestaurant()
                .getOwner()
                .getEmail();
        final String user = review.getUser()
                .getUsername();
        
        log.debug("Removing review with id {}", id);
        reviewReportRepository.deleteById(id);
        reviewRepository.deleteById(review.getId());
        emailService.sendEmailToSingleAddressee(restaurantEmail, EmailType.REVIEW_REPORT_POSITIVE, user);
    }
    
    @Override
    public void rejectRequest(@NonNull Long id) {
        final ReviewReport reviewReport = getReviewReport(id);
        final String restaurantEmail = reviewReport.getRestaurant()
                .getOwner()
                .getEmail();
        final String user = reviewReport.getReview()
                .getUser()
                .getUsername();
        
        reviewReportRepository.deleteById(id);
        emailService.sendEmailToSingleAddressee(restaurantEmail, EmailType.REVIEW_REPORT_NEGATIVE, user);
    }
    
    private ReviewReport getReviewReport(Long id) {
        return reviewReportRepository.findById(id)
                .orElseThrow(() -> new ReviewReportNotFoundException(MessageProvider.getMessage(MessageCode.Review.Report.REPORT_NOT_FOUND, id)));
    }
    
}
