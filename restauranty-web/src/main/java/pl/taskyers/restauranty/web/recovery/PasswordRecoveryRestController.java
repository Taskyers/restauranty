package pl.taskyers.restauranty.web.recovery;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.recovery.PasswordRecoveryService;
import pl.taskyers.restauranty.web.recovery.dto.PasswordRecoveryDTO;

import static pl.taskyers.restauranty.service.recovery.PasswordRecoveryService.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RECOVERY_PREFIX)
public class PasswordRecoveryRestController {
    
    private final PasswordRecoveryService passwordRecoveryService;
    
    @PostMapping(GENERATE_TOKEN)
    public ResponseEntity<ResponseMessage<String>> generateToken(@RequestParam final String email) {
        passwordRecoveryService.sendEmailWithToken(email);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.PasswordRecovery.TOKEN_GENERATED, MessageType.SUCCESS));
    }
    
    @PatchMapping(SET_PASSWORD)
    public ResponseEntity<ResponseMessage<String>> setPassword(@RequestBody final PasswordRecoveryDTO passwordRecoveryDTO) {
        passwordRecoveryService.setNewPassword(passwordRecoveryDTO.getToken(), passwordRecoveryDTO.getPassword());
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.PasswordRecovery.PASSWORD_SET, MessageType.SUCCESS));
    }
    
}
