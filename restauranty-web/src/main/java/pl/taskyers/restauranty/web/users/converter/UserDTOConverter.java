package pl.taskyers.restauranty.web.users.converter;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.users.entity.UserBase;
import pl.taskyers.restauranty.core.data.users.entity.UserClient;
import pl.taskyers.restauranty.web.users.dto.UserDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class UserDTOConverter {
    
    public Set<UserDTO> convertToDTOCollection(final Collection<UserBase> users) {
        final Set<UserDTO> result = new HashSet<>(users.size());
        users.forEach(userBase -> result.add(
                new UserDTO(userBase.getId(), userBase.getUsername(), userBase.getEmail(), userBase instanceof UserClient ? "CLIENT" : "RESTAURANT",
                        userBase.isEnabled())));
        return result;
    }
    
}
