package pl.taskyers.restauranty.core.data.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "chat_message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(targetEntity = UserBase.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private UserBase author;
    
    @OneToOne(targetEntity = UserBase.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient")
    private UserBase recipient;
    
    @Column(nullable = false)
    private Date timestamp;
    
    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    
    @ManyToOne(targetEntity = ChatRoom.class)
    @JoinColumn(name = "chat_room")
    private ChatRoom chatRoom;
    
    
}
