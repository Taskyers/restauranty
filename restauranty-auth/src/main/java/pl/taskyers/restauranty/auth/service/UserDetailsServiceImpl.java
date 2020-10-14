package pl.taskyers.restauranty.auth.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.entity.Role;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.repository.users.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserBase> userOptional = userRepository.findByUsername(username);
        if ( userOptional.isEmpty() ) {
            final String message = String.format("User with username: %s was not found", username);
            log.debug(message);
            throw new UsernameNotFoundException(message);
        }
        final UserBase user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), createRole(user.getRole()));
    }
    
    private Set<GrantedAuthority> createRole(Role role) {
        return Sets.newHashSet(new SimpleGrantedAuthority(role.getRole()
                .name()));
    }
    
}
   