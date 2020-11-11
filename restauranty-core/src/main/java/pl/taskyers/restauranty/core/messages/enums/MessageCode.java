package pl.taskyers.restauranty.core.messages.enums;

public interface MessageCode {
    
    String REGISTRATION_SUCCESSFUL = "Registration was successful";
    
    String ACCOUNT_WITH_FIELD_EXISTS = "Account with {0} {1} already exists";
    
    String FIELD_EMPTY = "{0} cannot be empty";
    
    String FIELD_INVALID_FORMAT = "Provided {0} is invalid";
    
    String PASSWORD_INVALID_FORMAT = "Current password is invalid";
    
    String NOT_FOUND = "{0} was not found";
    
    interface Restaurant {
        
        String RESTAURANT_WITH_FIELD_NOT_FOUND = "Restaurant with {0} {1} was not found";
        
        String RESTAURANT_WITH_FIELD_EXISTS = "Restaurant with {0} {1} already exists";
        
        String RESTAURANT_CREATED = "Restaurant has been created";
        
        String RESTAURANT_NOT_FOUND = "Restaurant with {0} {1} was not found";
        
        String RESTAURANT_NOT_YOURS = "Restaurant with {0} {1} is not yours";
        
        String RESTAURANT_UPDATED = "Restaurant has been updated";
        
        String RESTAURANT_DELETED = "Restaurant has been deleted";
        
    }
    
    interface PasswordRecovery {
        
        String TOKEN_GENERATED = "Token has been generated and email message has been sent";
        
        String PASSWORD_SET = "Password has been set";
        
    }
    
    interface Images {
        
        String IMAGE_EXISTS = "Image with provided name already exists";
        
        String INVALID_EXTENSION = "Extension of image is invalid. Provided {0}, expected: png|jpg|jpeg";
        
        String IMAGE_UPLOADED = "Image has been uploaded";
        
        String IMAGE_DELETED = "Image has been deleted";
        
        String IMAGE_NOT_FOUND = "Image with {0} name was not found";
        
        String ERROR_UPLOAD = "Error while uploading image";
        
        String ERROR_DOWNLOAD = "Error while downloading image";
        
        String ERROR_DELETING = "Error while deleting image";
        
    }
    
    interface Review {
        
        String RATE_NOT_FOUND = "Rate with value {0} was not found";
        
        String REVIEW_ADDED = "Review has been added";
        
        String REVIEW_UPDATED = "Review has been updated";
        
        String REVIEW_NOT_FOUND = "Review with {0} {1} was not found";
        
        String REVIEW_NOT_YOURS = "Review is not yours";
        
        String REVIEW_DELETED = "Review has been deleted";
        
        interface Report {
            
            String REPORT_EXISTS = "Report for this review already exists";
            
            String REPORT_SENT = "Report has been sent";
            
        }
        
    }
    
    interface ChatMessage {
        
        String AUTHOR_IS_NOT_YOU = "Chat message is not from you";
        
        String RECIPIENT_NOT_FOUND = "Chat message recipient with {0} {1} not found";
    
        String AUTHOR_NOT_FOUND = "Chat message author with {0} {1} not found";
        
        String WRONG_RECIPIENT = "You cannot sent message to this recipient";
        
    }
    
    interface ChatRoom {
        
        String CHAT_ROOM_NOT_FOUND = "There are no chat messages between {0} and {1}";
        
    }
    
}
