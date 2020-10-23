package pl.taskyers.restauranty.service.impl.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.auth.AuthProvider;

@Service
@RequiredArgsConstructor
public class AuthProviderImpl implements AuthProvider {
    
    private final UserRepository userRepository;
    
    @Override
    public String getUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @Override
    public UserBase getUserEntity() {
        return userRepository.findByUsername(getUserLogin()).get();
    }
    
    @Override
    public boolean isLoggedIn() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !("anonymousUser").equals(authentication.getName());
    }
    
}
