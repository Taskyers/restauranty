package pl.taskyers.restauranty.service.impl.images.validator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.images.RestaurantImageRepository;

@Component
@RequiredArgsConstructor
public class RestaurantImageValidator {
    
    private final RestaurantImageRepository restaurantImageRepository;
    
    public ValidationMessageContainer validate(final MultipartFile image) {
        final ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        validateExtension(image, validationMessageContainer);
        validateName(image, validationMessageContainer);
        return validationMessageContainer;
    }
    
    private void validateExtension(MultipartFile image, ValidationMessageContainer validationMessageContainer) {
        final String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        if ( !extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg") ) {
            validationMessageContainer.addError(MessageProvider.getMessage(MessageCode.Images.INVALID_EXTENSION, extension), "extension");
        }
    }
    
    private void validateName(MultipartFile image, ValidationMessageContainer validationMessageContainer) {
        if ( restaurantImageRepository.findByName(image.getOriginalFilename())
                .isPresent() ) {
            validationMessageContainer.addError(MessageCode.Images.IMAGE_EXISTS, "name");
        }
    }
    
}
