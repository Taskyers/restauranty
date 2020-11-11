package pl.taskyers.restauranty.web.reviews.converter;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.data.reviews.entity.ReviewReport;
import pl.taskyers.restauranty.web.reviews.dto.AdminReviewReportDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class AdminReviewReportDTOConverter {
    
    public Set<AdminReviewReportDTO> convertReports(List<ReviewReport> reviewReports) {
        final Set<AdminReviewReportDTO> result = new HashSet<>(reviewReports.size());
        for ( ReviewReport reviewReport : reviewReports ) {
            final Review review = reviewReport.getReview();
            result.add(new AdminReviewReportDTO(reviewReport.getId(), review.getUser()
                    .getUsername(), review.getContent(), reviewReport.getRestaurant()
                    .getName()));
        }
        return result;
    }
    
}
