package pl.taskyers.restauranty.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.data.chat.entity.MessageStatus;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Long countByAuthorAndRecipientAndStatus(UserBase author, UserBase recipient, MessageStatus status);
    
}
