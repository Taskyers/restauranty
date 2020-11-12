package pl.taskyers.restauranty.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.chat.entity.ChatRoom;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    Optional<ChatRoom> findByClientAndRestaurant(UserBase client, UserBase restaurant);
    
    @Query(value = "select * from chat_room where client=?1 or restaurant=?1", nativeQuery = true)
    List<ChatRoom> findChatsByUserId(Long id);
    
}
