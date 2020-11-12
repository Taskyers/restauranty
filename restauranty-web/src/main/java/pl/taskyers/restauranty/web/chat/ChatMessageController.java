package pl.taskyers.restauranty.web.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.restauranty.core.data.chat.dto.ChatMessageDTO;
import pl.taskyers.restauranty.core.utils.DateUtils;
import pl.taskyers.restauranty.service.chat.ChatMessageService;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    private final ChatMessageService chatMessageService;
    
    @MessageMapping("/secured/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
        
        chatMessageService.sendMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/queue/messages",
                new ChatMessageDTO(chatMessage.getAuthor(), chatMessage.getRecipient(), chatMessage.getContent(), DateUtils.parseString(new Date())));
    }
    
    @GetMapping(ChatMessageService.CHAT_MESSAGE_PREFIX + "/{recipient}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String recipient) {
        
        return ResponseEntity
                .ok(chatMessageService.countNewMessages(recipient));
    }
    
    @GetMapping(ChatMessageService.CHAT_MESSAGE_PREFIX + "/{recipient}")
    public ResponseEntity<List<ChatMessageDTO>> findChatMessages(
            @PathVariable String recipient) {
        return ResponseEntity
                .ok(chatMessageService.getChatMessages(recipient));
    }
    
}
