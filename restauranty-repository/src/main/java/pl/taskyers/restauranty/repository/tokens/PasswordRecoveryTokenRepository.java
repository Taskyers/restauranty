package pl.taskyers.restauranty.repository.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.tokens.entity.PasswordRecoveryToken;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;

import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {
    
    Optional<PasswordRecoveryToken> findByToken(String token);
    
    PasswordRecoveryToken findByUser(UserBase user);
    
}
