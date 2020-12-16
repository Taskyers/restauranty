package pl.taskyers.restauranty.service.clients;

import lombok.NonNull;

import java.util.Set;

public interface RestaurantSearchService {
    
    String PREFIX = "/client";
    
    String SEARCH = "/search";
    
    String GET_RESTAURANTS_OPEN_HOURS = "/openHours/{restaurantName}";
    
    /**
     * Search for restaurants by name and tags. Following cases are covered:
     * <ol>
     *     <li>Name and tags are not provided - return all restaurants</li>
     *     <li>Only name is provided - return all restaurants which contain name</li>
     *     <li>Only tags are provided - return all restaurants which have tags</li>
     *     <li>Both name and tags are provided - return all restaurants which contain name and have tags</li>
     * </ol>
     *
     * @param restaurantName restaurant's name
     * @param tags           tags as {@link Set} of strings
     * @return {@link Set} with only restaurants name as strings
     * @since 1.0.0
     */
    Set<String> searchForRestaurants(final @NonNull String restaurantName, final @NonNull Set<String> tags);
    
}
