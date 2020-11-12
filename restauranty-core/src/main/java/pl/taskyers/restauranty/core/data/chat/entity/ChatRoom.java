package pl.taskyers.restauranty.core.data.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "chat_room")
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(targetEntity = UserBase.class)
    @JoinColumn(name = "restaurant")
    private UserBase restaurant;
    
    @OneToOne(targetEntity = UserBase.class)
    @JoinColumn(name = "client")
    private UserBase client;
    
    
    @OneToMany(targetEntity = ChatMessage.class, mappedBy = "chatRoom", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<ChatMessage> messages = new HashSet<>();
    
    
}
