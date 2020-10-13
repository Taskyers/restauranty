package pl.taskyers.restauranty.auth.enums;

public interface SecurityConstants {
    
    /**
     * Secret for JWT algorithm
     */
    String SECRET = "m,xbnvgjbtfro3w4ryh523409523-04124=123-47312459-0231y4sjkvblxcvoisgfweq[er1238-04y13740-923y409234o";
    
    /**
     * Expiration time for JWT Token in milliseconds
     */
    long EXPIRATION_TIME = 86400000; // 24h
    
    /**
     * Bearer token's prefix
     */
    String TOKEN_PREFIX = "Bearer ";
    
    /**
     * Authorization header
     */
    String HEADER_STRING = "Authorization";
    
}