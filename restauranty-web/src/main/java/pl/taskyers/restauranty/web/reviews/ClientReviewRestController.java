package pl.taskyers.restauranty.web.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.reviews.ClientReviewService;
import pl.taskyers.restauranty.service.reviews.RestaurantReviewService;
import pl.taskyers.restauranty.web.reviews.converter.RestaurantClientReviewDTOConverter;
import pl.taskyers.restauranty.web.reviews.dto.ClientReviewDTO;
import pl.taskyers.restauranty.web.reviews.dto.RestaurantClientReviewDTO;
import pl.taskyers.restauranty.web.reviews.dto.UpdateReviewDTO;
import pl.taskyers.restauranty.web.util.UriUtils;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(ClientReviewService.PREFIX)
public class ClientReviewRestController {
    
    private final ClientReviewService clientReviewService;
    
    private final RestaurantReviewService restaurantReviewService;
    
    private final RestaurantClientReviewDTOConverter restaurantClientReviewDTOConverter;
    
    @GetMapping(ClientReviewService.BY_RESTAURANT)
    public ResponseEntity<Set<RestaurantClientReviewDTO>> getReviewsForRestaurant(@PathVariable final String restaurant) {
        final List<Review> reviews = restaurantReviewService.getReviewsForRestaurant(restaurant);
        return ResponseEntity.ok(restaurantClientReviewDTOConverter.convertToResponseDTO(reviews));
    }
    
    @PostMapping
    public ResponseEntity<ResponseMessage<RestaurantClientReviewDTO>> addReview(@RequestBody final ClientReviewDTO clientReviewDTO) {
        final Review savedReview =
                clientReviewService.addReview(clientReviewDTO.getRestaurant(), clientReviewDTO.getContent(), clientReviewDTO.getRate());
        return ResponseEntity.created(UriUtils.createURIFromId(savedReview.getId()))
                .body(new ResponseMessage<>(MessageCode.Review.REVIEW_ADDED, MessageType.SUCCESS,
                        restaurantClientReviewDTOConverter.convertReview(savedReview)));
    }
    
    @PutMapping(ClientReviewService.BY_ID)
    public ResponseEntity<ResponseMessage<RestaurantClientReviewDTO>> updateReview(@PathVariable final Long id,
            @RequestBody final UpdateReviewDTO updateReviewDTO) {
        final Review updatedReview = clientReviewService.updateReview(id, updateReviewDTO.getContent(), updateReviewDTO.getRate());
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.REVIEW_UPDATED, MessageType.SUCCESS,
                restaurantClientReviewDTOConverter.convertReview(updatedReview)));
    }
    
    @DeleteMapping(ClientReviewService.BY_ID)
    public ResponseEntity<ResponseMessage<String>> deleteReview(@PathVariable final Long id) {
        clientReviewService.deleteReview(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.REVIEW_DELETED, MessageType.SUCCESS));
    }
    
}
