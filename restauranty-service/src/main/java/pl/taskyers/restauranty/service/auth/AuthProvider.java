package pl.taskyers.restauranty.service.auth;

import pl.taskyers.restauranty.core.data.users.entity.UserBase;

public interface AuthProvider {
    
    String getUserLogin();
    
    UserBase getUserEntity();
    
    boolean isLoggedIn();
    
}
