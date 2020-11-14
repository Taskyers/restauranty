package pl.taskyers.restauranty.web.images;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.service.images.ClientImageService;
import pl.taskyers.restauranty.service.images.ImageStorageService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ClientImageService.CLIENT_IMAGE_PREFIX)
public class ClientImageRestController {
    
    private final ClientImageService clientImageService;
    
    private final ImageStorageService imageStorageService;
    
    @GetMapping(ClientImageService.BY_RESTAURANT)
    public ResponseEntity<Resource> getMainImageForRestaurant(@PathVariable final String restaurant) {
        final RestaurantImage restaurantImage = clientImageService.getMainImageForRestaurant(restaurant);
        final Resource image = imageStorageService.load(restaurantImage);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(restaurantImage.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }
    
}
