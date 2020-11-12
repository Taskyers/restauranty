package pl.taskyers.restauranty.web.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.restauranty.service.chat.ChatRoomService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ChatRoomService.CHAT_ROOM_PREFIX)
public class ChatRoomController {
    
    private final ChatRoomService chatRoomService;
    
    @GetMapping
    public ResponseEntity<?> findCurrentUsersChatRooms() {
        return ResponseEntity.ok(chatRoomService.getCurrentUserChatRooms());
    }
    
}
