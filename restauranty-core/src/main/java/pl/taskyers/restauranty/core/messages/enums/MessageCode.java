package pl.taskyers.restauranty.core.messages.enums;

public interface MessageCode {
    
    String REGISTRATION_SUCCESSFUL = "Registration was successful";
    
    String ACCOUNT_WITH_FIELD_EXISTS = "Account with {0} {1} already exists";
    
    String FIELD_EMPTY = "{0} cannot be empty";
    
    String FIELD_INVALID_FORMAT = "Provided {0} is invalid";
    
    String PASSWORD_INVALID_FORMAT = "Current password is invalid";
    
    String NOT_FOUND = "{0} was not found";
    
    interface Users {
        
        String USER_BANNED = "User has been banned";
        
        String USER_UNBANNED = "User has been unbanned";
        
    }
    
    interface Restaurant {
        
        String RESTAURANT_WITH_FIELD_NOT_FOUND = "Restaurant with {0} {1} was not found";
        
        String RESTAURANT_WITH_FIELD_EXISTS = "Restaurant with {0} {1} already exists";
        
        String RESTAURANT_CREATED = "Restaurant has been created";
        
        String RESTAURANT_NOT_FOUND = "Restaurant with {0} {1} was not found";
        
        String RESTAURANT_NOT_YOURS = "Restaurant is not yours";
        
        String RESTAURANT_UPDATED = "Restaurant has been updated";
        
        String RESTAURANT_DELETED = "Restaurant has been deleted";
        
        String RESTAURANT_VERIFIED = "Restaurant has been verified";
        
    }
    
    interface Menu {
        
        String DISH_ADDED = "Dish has been added";
        
        String DISH_EDITED = "Dish has been edited";
        
        String DISH_REMOVED = "Dish has been removed";
        
        String FIELD_INVALID = "Field: {0} has invalid value ({1})";
        
        String NAME_EXISTS = "Dish with provided name already exists";
        
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
        
        String MAIN_IMAGE_SET = "Image has been set as main";
        
        String MAIN_IMAGE_NOT_FOUND = "Restaurant {0} has no main image";
        
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
            
            String REPORT_NOT_FOUND = "Review report with id {0} was not found";
            
            String REPORT_ACCEPTED = "Review report has been accepted";
            
            String REPORT_REJECTED = "Review report has been rejected";
            
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
    
    interface OpenHour{
        
        String OPEN_TIME_AFTER_CLOSE_TIME = "Open time cannot be after close time";
    
    }
     interface Reservation{
    
         String RESERVATION_CREATED = "Reservation has been created";
    
         String RESERVATION_NOT_ALLOWED = "Reservation is not allowed at this time with this amount of persons";
         
         String RESERVATION_INVALID_TIME = "Reservation time is not between open hours of the restaurant";
    
         String RESERVATION_NOT_YOURS = "Reservation is not yours";
    
         String RESERVATION_NOT_FOUND = "Reservation with {0} {1} was not found";
    
         String RESERVATION_ACCEPTED = "Reservation has been accepted";
    
         String RESERVATION_CANCELED = "Reservation has been canceled";
    
         String RESERVATION_REJECTED = "Reservation has been rejected";
     }
}
