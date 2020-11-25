package pl.taskyers.restauranty.web.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.core.messages.enums.MessageType;
import pl.taskyers.restauranty.service.users.AdminUsersService;
import pl.taskyers.restauranty.web.users.converter.UserDTOConverter;
import pl.taskyers.restauranty.web.users.dto.UserDTO;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(AdminUsersService.PREFIX)
public class AdminUsersRestController {
    
    private final AdminUsersService adminUsersService;
    
    @GetMapping
    public ResponseEntity<Set<UserDTO>> getUsers() {
        return ResponseEntity.ok(UserDTOConverter.convertToDTOCollection(adminUsersService.getUsers()));
    }
    
    @PatchMapping(AdminUsersService.BAN_USER + AdminUsersService.BY_ID)
    public ResponseEntity<ResponseMessage<?>> banUser(@PathVariable final Long id) {
        adminUsersService.banUser(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Users.USER_BANNED, MessageType.SUCCESS));
    }
    
    @PatchMapping(AdminUsersService.UNBAN_USER + AdminUsersService.BY_ID)
    public ResponseEntity<ResponseMessage<?>> unbanUser(@PathVariable final Long id) {
        adminUsersService.unbanUser(id);
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.Users.USER_UNBANNED, MessageType.SUCCESS));
    }
    
}
