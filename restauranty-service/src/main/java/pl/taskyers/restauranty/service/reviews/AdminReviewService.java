package pl.taskyers.restauranty.service.reviews;

import lombok.NonNull;
import pl.taskyers.restauranty.core.data.reviews.ReviewReportNotFoundException;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;

import java.util.List;

public interface AdminReviewService {
    
    String PREFIX = "/admin/reviews";
    
    String ACCEPT = "/accept/{id}";
    
    String REJECT = "/reject/{id}";
    
    /**
     * Get all reviews that has been reported
     *
     * @return {@link List} of {@link ReviewReport}
     * @since 1.0.0
     */
    List<ReviewReport> getReportedReviews();
    
    /**
     * Remove reported review by id (accept remove request)
     *
     * @param id review report's id
     * @throws ReviewReportNotFoundException if review report was not found
     * @since 1.0.0
     */
    void removeReview(@NonNull final Long id) throws ReviewReportNotFoundException;
    
    /**
     * Reject remove review request by id
     *
     * @param id review report's id
     * @throws ReviewReportNotFoundException if review report was not found
     * @since 1.0.0
     */
    void rejectRequest(@NonNull final Long id) throws ReviewReportNotFoundException;
    
}
