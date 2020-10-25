package pl.taskyers.restauranty.web.reviews;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.reviews.entity.Review;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.reviews.ClientReviewService;
import pl.taskyers.restauranty.web.reviews.dto.ClientReviewDTO;
import pl.taskyers.restauranty.web.reviews.dto.UpdateReviewDTO;
import pl.taskyers.restauranty.web.util.UriUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(ClientReviewService.PREFIX)
public class ClientReviewRestController {
    
    private final ClientReviewService clientReviewService;
    
    @PostMapping
    public ResponseEntity<ResponseMessage<ClientReviewDTO>> addReview(@RequestBody final ClientReviewDTO clientReviewDTO) {
        final Review savedReview = clientReviewService.addReview(clientReviewDTO.getRestaurant(), clientReviewDTO.getContent(), clientReviewDTO.getRate());
        return ResponseEntity.created(UriUtils.createURIFromId(savedReview.getId()))
                .body(new ResponseMessage<>(MessageCode.Review.REVIEW_ADDED, MessageType.SUCCESS, clientReviewDTO));
    }
    
    @PutMapping(ClientReviewService.BY_ID)
    public ResponseEntity<ResponseMessage<ClientReviewDTO>> updateReview(@PathVariable final Long id, @RequestBody final UpdateReviewDTO updateReviewDTO) {
        final Review updatedReview = clientReviewService.updateReview(id, updateReviewDTO.getContent(), updateReviewDTO.getRate());
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.REVIEW_UPDATED, MessageType.SUCCESS, new ClientReviewDTO(
                updatedReview.getRestaurant()
                        .getName(), updatedReview.getContent(), updatedReview.getRate()
                .getValue())));
    }
    
    @DeleteMapping(ClientReviewService.BY_ID)
    public ResponseEntity<ResponseMessage<String>> deleteReview(@PathVariable final Long id) {
        clientReviewService.deleteReview(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Review.REVIEW_DELETED, MessageType.SUCCESS));
    }
    
}
