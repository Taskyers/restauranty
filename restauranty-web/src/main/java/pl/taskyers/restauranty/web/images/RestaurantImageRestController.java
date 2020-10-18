package pl.taskyers.restauranty.web.images;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.images.ImageStorageService;
import pl.taskyers.restauranty.service.images.RestaurantImageService;
import pl.taskyers.restauranty.web.images.converter.ImageResponseDTOConverter;
import pl.taskyers.restauranty.web.images.dto.ImageResponseDTO;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestaurantImageService.RESTAURANT_IMAGE_PREFIX)
public class RestaurantImageRestController {
    
    private final RestaurantImageService restaurantImageService;
    
    private final ImageStorageService imageStorageService;
    
    @GetMapping(RestaurantImageService.BY_RESTAURANT)
    public ResponseEntity<List<ImageResponseDTO>> getImagesForRestaurant(@PathVariable final String restaurant) {
        final Set<RestaurantImage> images = restaurantImageService.getAllForRestaurant(restaurant);
        return ResponseEntity.ok(ImageResponseDTOConverter.convertToDTO(images));
    }
    
    @GetMapping(RestaurantImageService.GET_BY_NAME)
    public ResponseEntity<Resource> getAllImages(@PathVariable final String name) {
        final RestaurantImage restaurantImage = restaurantImageService.getImage(name);
        final Resource image = imageStorageService.load(restaurantImage);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(restaurantImage.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }
    
    @PostMapping(RestaurantImageService.BY_RESTAURANT)
    public ResponseEntity<ResponseMessage<ImageResponseDTO>> uploadImage(@RequestParam final MultipartFile image,
            @PathVariable final String restaurant) {
        final RestaurantImage restaurantImage = restaurantImageService.saveImage(image, restaurant);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Images.IMAGE_UPLOADED, MessageType.SUCCESS,
                new ImageResponseDTO(restaurantImage.getName(), restaurantImage.getType(), restaurantImage.getSize())));
    }
    
    @DeleteMapping(RestaurantImageService.BY_NAME)
    public ResponseEntity<ResponseMessage<String>> deleteImage(@PathVariable final String name) {
        restaurantImageService.deleteImage(name);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Images.IMAGE_DELETED, MessageType.SUCCESS));
    }
    
}
