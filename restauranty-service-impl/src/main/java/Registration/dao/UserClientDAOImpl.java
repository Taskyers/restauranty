package Registration.dao;

import dao.UserClientDAO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.repository.users.ClientUserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserClientDAOImpl implements UserClientDAO {
    
    private final ClientUserRepository clientUserRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserClient registerUser(UserClient userClientEntity) {
        userClientEntity.setPassword(passwordEncoder.encode(userClientEntity.getPassword()));
        return clientUserRepository.save(userClientEntity);
    }
    
    @Override
    public Optional<UserClient> getEntityByEmail(String email) {
        return clientUserRepository.findByEmail(email);
    }
    
    @Override
    public Optional<UserClient> getEntityByUsername(String username) {
        return clientUserRepository.findByUsername(username);
    }
    
    @Override
    public Optional<UserClient> getEntityById(Long id) {
        return clientUserRepository.findById(id);
    }
    
}
