package pl.taskyers.restauranty.service.impl.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.enums.RoleType;
import pl.taskyers.restauranty.repository.users.UserRepository;
import pl.taskyers.restauranty.service.users.AdminUsersService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUsersServiceImpl implements AdminUsersService {
    
    private final UserRepository userRepository;
    
    @Override
    public Collection<UserBase> getUsers() {
        final List<UserBase> users = userRepository.findAll();
        return users.stream()
                .filter(userBase -> !userBase.getRole()
                        .getRole()
                        .equals(RoleType.ROLE_ADMIN))
                .collect(Collectors.toSet());
    }
    
    @Override
    public UserBase banUser(@NonNull Long id) throws UserNotFoundException {
        log.debug("Banning user: {}", id);
        return setEnabledAndSave(id, false);
    }
    
    @Override
    public UserBase unbanUser(@NonNull Long id) throws UserNotFoundException {
        log.debug("Unbanning user: {}", id);
        return setEnabledAndSave(id, true);
    }
    
    private UserBase setEnabledAndSave(Long id, boolean enabled) {
        final UserBase user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s was not found", id)));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }
    
}
