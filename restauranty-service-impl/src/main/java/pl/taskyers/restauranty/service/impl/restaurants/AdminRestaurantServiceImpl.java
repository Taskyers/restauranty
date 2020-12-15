package pl.taskyers.restauranty.service.impl.restaurants;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.core.data.users.UserNotFoundException;
import pl.taskyers.restauranty.core.data.users.entity.UserRestaurant;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.users.UserRestaurantRepository;
import pl.taskyers.restauranty.service.restaurants.AdminRestaurantService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminRestaurantServiceImpl implements AdminRestaurantService {
    
    private final UserRestaurantRepository userRestaurantRepository;
    
    @Override
    public Collection<UserRestaurant> getNotVerifiedRestaurants() {
        return userRestaurantRepository.findAll()
                .stream()
                .filter(restaurant -> !restaurant.isVerified())
                .collect(Collectors.toSet());
    }
    
    @Override
    public UserRestaurant verify(@NonNull Long id) throws UserNotFoundException {
        final UserRestaurant userRestaurant = userRestaurantRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(MessageProvider.getMessage(
                        MessageCode.NOT_FOUND, String.format("User with id: %s", id))));
        userRestaurant.setVerified(true);
        log.debug("Verifying restaurant: {}", id);
        return userRestaurantRepository.save(userRestaurant);
    }
    
}
