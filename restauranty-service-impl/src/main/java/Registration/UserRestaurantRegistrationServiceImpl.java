package Registration;

import Registration.Validator.RegistrationValidator;
import dao.UserRestaurantDAO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.converters.AccountConverter;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.data.users.messages.ResponseMessage;
import pl.taskyers.restauranty.core.data.users.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.data.users.utils.UriUtils;

import static pl.taskyers.restauranty.core.data.users.messages.MessageCode.REGISTRATION_SUCCESSFUL;
import static pl.taskyers.restauranty.core.data.users.messages.MessageProvider.getMessage;
import static pl.taskyers.restauranty.core.data.users.messages.MessageType.SUCCESS;

@Service("userRestaurantService")
@AllArgsConstructor
public class UserRestaurantRegistrationServiceImpl implements RegistrationService {
    
    private final RegistrationValidator registrationValidator;
    
    private final UserRestaurantDAO userRestaurantDAO;
    
    @Override
    public ResponseEntity register(AccountDTO accountDTO) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(accountDTO, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        
        UserRestaurant savedUser = userRestaurantDAO.registerUser(AccountConverter.convertFromDTOToRestaurant(accountDTO));
        
        return ResponseEntity.created(UriUtils.createURIFromId(savedUser.getId()))
                .body(new ResponseMessage<>(getMessage(REGISTRATION_SUCCESSFUL), SUCCESS, savedUser));
    }
    
    @Override
    public boolean accountExistsByEmail(String email) {
        return userRestaurantDAO.getEntityByEmail(email).isPresent();
    }
    
    @Override
    public boolean accountExistsByUsername(String username) {
        return userRestaurantDAO.getEntityByUsername(username).isPresent();
    }
    
}
