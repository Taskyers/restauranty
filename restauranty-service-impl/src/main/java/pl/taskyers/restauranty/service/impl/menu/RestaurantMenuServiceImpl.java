package pl.taskyers.restauranty.service.impl.menu;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.taskyers.restauranty.core.data.menu.DishNotFoundException;
import pl.taskyers.restauranty.core.data.menu.entity.SingleMenuDish;
import pl.taskyers.restauranty.core.data.menu.enums.DishType;
import pl.taskyers.restauranty.core.data.restaurants.RestaurantNotFoundException;
import pl.taskyers.restauranty.core.data.restaurants.entity.Restaurant;
import pl.taskyers.restauranty.core.error.exceptions.ValidationException;
import pl.taskyers.restauranty.core.messages.MessageProvider;
import pl.taskyers.restauranty.core.messages.container.ValidationMessageContainer;
import pl.taskyers.restauranty.core.messages.enums.MessageCode;
import pl.taskyers.restauranty.repository.menu.MenuRepository;
import pl.taskyers.restauranty.service.impl.menu.validator.RestaurantMenuValidator;
import pl.taskyers.restauranty.service.menu.RestaurantMenuService;
import pl.taskyers.restauranty.service.menu.dto.AddDishDTO;
import pl.taskyers.restauranty.service.restaurants.RestaurantService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantMenuServiceImpl implements RestaurantMenuService {
    
    private final RestaurantService restaurantService;
    
    private final MenuRepository menuRepository;
    
    @Override
    public Set<SingleMenuDish> getMenuForRestaurant(@NonNull String restaurant) throws RestaurantNotFoundException {
        return restaurantService.getRestaurant(restaurant)
                .getMenu();
    }
    
    @Override
    public Set<SingleMenuDish> getDishesForRestaurantAndType(@NonNull String restaurant, @NonNull DishType dishType)
            throws RestaurantNotFoundException {
        final Restaurant restaurantFromDatabase = restaurantService.getRestaurant(restaurant);
        return restaurantFromDatabase.getMenu()
                .stream()
                .filter(singleMenuDish -> singleMenuDish.getType() == dishType)
                .collect(Collectors.toSet());
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SingleMenuDish addDishToMenu(@NonNull String restaurant, @NonNull AddDishDTO addDishDTO)
            throws RestaurantNotFoundException, ValidationException {
        final Restaurant restaurantFromDatabase = restaurantService.getRestaurant(restaurant);
        final Set<SingleMenuDish> menu = restaurantFromDatabase.getMenu();
        final ValidationMessageContainer validationMessageContainer =
                RestaurantMenuValidator.validateWithDuplicates(menu, addDishDTO);
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        final SingleMenuDish singleMenuDish = addDishDTO.toEntity();
        menu.add(singleMenuDish);
        return singleMenuDish;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SingleMenuDish editDish(@NonNull Long id, @NonNull String restaurant, @NonNull AddDishDTO addDishDTO)
            throws DishNotFoundException, RestaurantNotFoundException, ValidationException {
        final SingleMenuDish dishFromDatabase = getDish(id);
        final Restaurant restaurantFromDatabase = restaurantService.getRestaurant(restaurant);
        final Set<SingleMenuDish> menu = restaurantFromDatabase.getMenu();
        
        ValidationMessageContainer validationMessageContainer;
        if ( dishFromDatabase.getName()
                .equals(addDishDTO.getName()) ) {
            validationMessageContainer = RestaurantMenuValidator.validate(addDishDTO);
        } else {
            validationMessageContainer = RestaurantMenuValidator.validateWithDuplicates(menu, addDishDTO);
        }
        if ( validationMessageContainer.hasErrors() ) {
            throw new ValidationException(validationMessageContainer.getErrors());
        }
        
        return copyData(dishFromDatabase, addDishDTO);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeDish(@NonNull Long id, @NonNull String restaurant) throws RestaurantNotFoundException, DishNotFoundException {
        final SingleMenuDish dishFromDatabase = getDish(id);
        final Restaurant restaurantFromDatabase = restaurantService.getRestaurant(restaurant);
        restaurantFromDatabase.getMenu()
                .remove(dishFromDatabase);
        menuRepository.deleteById(id);
    }
    
    private SingleMenuDish copyData(SingleMenuDish existingDish, AddDishDTO addDishDTO) {
        existingDish.setName(addDishDTO.getName());
        existingDish.setDescription(addDishDTO.getDescription());
        existingDish.setPrice(addDishDTO.getPrice());
        existingDish.setType(addDishDTO.getType());
        return existingDish;
    }
    
    private SingleMenuDish getDish(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException(MessageProvider.getMessage(
                        MessageCode.NOT_FOUND, "Dish")));
    }
    
}
