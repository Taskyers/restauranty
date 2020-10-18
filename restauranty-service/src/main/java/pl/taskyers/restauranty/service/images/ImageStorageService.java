package pl.taskyers.restauranty.service.images;

import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.error.exceptions.ServerErrorException;

public interface ImageStorageService {
    
    /**
     * Store {@link MultipartFile} in file system
     *
     * @param image image to be stored
     * @return absolute path to file in file system
     * @throws ServerErrorException if error has occurred
     * @since 1.0.0
     */
    String store(@NonNull final MultipartFile image) throws ServerErrorException;
    
    /**
     * Load image as web {@link Resource} from file system
     *
     * @param image {@link RestaurantImage}
     * @return Image as {@link Resource}
     * @throws ServerErrorException if {@link java.net.URI is malformed}
     * @since 1.0.0
     */
    Resource load(@NonNull final RestaurantImage image) throws ServerErrorException;
    
    /**
     * Delete image from file system
     *
     * @param file file's name
     * @throws ServerErrorException if error has occurred
     * @since 1.0.0
     */
    void delete(@NonNull final String file) throws ServerErrorException;
    
}
