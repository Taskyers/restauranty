package pl.taskyers.restauranty.core.data.tokens.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import javax.persistence.*;

@Entity
@Table(name = "password_recovery_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(targetEntity = UserBase.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "`user`", unique = true)
    private UserBase user;
    
    @Column(unique = true)
    private String token;
    
}