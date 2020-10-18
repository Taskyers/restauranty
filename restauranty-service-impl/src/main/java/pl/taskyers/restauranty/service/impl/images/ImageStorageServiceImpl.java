package pl.taskyers.restauranty.service.impl.images;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.data.images.entity.RestaurantImage;
import pl.taskyers.restauranty.core.error.exceptions.ServerErrorException;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.service.images.ImageStorageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class ImageStorageServiceImpl implements ImageStorageService {
    
    private static final String DIRECTORY = "default";
    
    private final Path storageLocation;
    
    public ImageStorageServiceImpl() throws IOException {
        storageLocation = Paths.get(DIRECTORY);
        Files.createDirectories(storageLocation);
    }
    
    @Override
    public String store(@NonNull final MultipartFile image) {
        try {
            final String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            final Path target = storageLocation.resolve(fileName)
                    .normalize();
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch ( IOException e ) {
            log.error("{} : {}", MessageCode.Images.ERROR_UPLOAD, e);
            throw new ServerErrorException(MessageCode.Images.ERROR_UPLOAD);
        }
    }
    
    @Override
    public Resource load(@NonNull final RestaurantImage image) {
        try {
            final Path filePath = storageLocation.resolve(image.getName())
                    .normalize();
            return new UrlResource(filePath.toUri());
        } catch ( MalformedURLException e ) {
            log.error("{} : {}", MessageCode.Images.ERROR_DOWNLOAD, e);
            throw new ServerErrorException(MessageCode.Images.ERROR_DOWNLOAD);
        }
    }
    
    @Override
    public void delete(@NonNull final String file) {
        try {
            final Path filePath = storageLocation.resolve(file)
                    .normalize();
            Files.deleteIfExists(filePath);
        } catch ( IOException e ) {
            log.error("{} : {}", MessageCode.Images.ERROR_DELETING, e);
            throw new ServerErrorException(MessageCode.Images.ERROR_DELETING);
        }
    }
    
}
