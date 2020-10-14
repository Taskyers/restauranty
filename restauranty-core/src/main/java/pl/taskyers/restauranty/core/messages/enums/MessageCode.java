package pl.taskyers.restauranty.core.messages.enums;

public interface MessageCode {
    
    String REGISTRATION_SUCCESSFUL = "Registration was successful.";
    
    String ACCOUNT_WITH_FIELD_EXISTS = "Account with {0} {1} already exists.";
    
    String FIELD_EMPTY = "{0} cannot be empty";
    
    String FIELD_INVALID_FORMAT = "Provided {0} is invalid";
    
    String PASSWORD_INVALID_FORMAT = "Current password is invalid";
    
    String NOT_FOUND = "{0} was not found";
    
    interface PasswordRecovery {
        
        String TOKEN_GENERATED = "Token has been generated and email message has been sent";
        
        String PASSWORD_SET = "Password has been set";
        
    }
    
}
