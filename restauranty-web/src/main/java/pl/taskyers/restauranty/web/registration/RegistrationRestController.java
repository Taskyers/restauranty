package pl.taskyers.restauranty.web.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.data.users.dto.AccountDTO;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.registration.RegistrationService;
import pl.taskyers.restauranty.web.registration.dto.RegistrationResponseDTO;
import pl.taskyers.restauranty.web.util.UriUtils;

import static pl.taskyers.restauranty.service.registration.RegistrationService.*;

@RestController
@RequestMapping(value = REGISTRATION_PREFIX)
@RequiredArgsConstructor
public class RegistrationRestController {
    
    private final RegistrationService registrationService;
    
    @PostMapping
    public ResponseEntity<ResponseMessage<RegistrationResponseDTO>> register(@RequestBody AccountDTO accountDTO) {
        final UserBase savedUser = registrationService.register(accountDTO);
        final RegistrationResponseDTO registrationResponseDTO = new RegistrationResponseDTO(savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getRole()
                        .getRole());
        return ResponseEntity.created(UriUtils.createURIFromId(savedUser.getId()))
                .body(new ResponseMessage<>(MessageProvider.getMessage(MessageCode.REGISTRATION_SUCCESSFUL), MessageType.SUCCESS,
                        registrationResponseDTO));
    }
    
    @GetMapping(FIND_BY_USERNAME)
    public boolean userExistsByUsername(@PathVariable String username) {
        return registrationService.accountExistsByUsername(username);
    }
    
    @GetMapping(FIND_BY_EMAIL)
    public boolean userExistsByEmail(@PathVariable String email) {
        return registrationService.accountExistsByEmail(email);
    }
    
}
