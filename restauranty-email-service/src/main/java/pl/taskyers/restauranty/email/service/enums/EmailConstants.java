package pl.taskyers.restauranty.email.service.enums;

public interface EmailConstants {
    
    String SENDER_ADDRESS = "noreply@restauranty.com";
    
    String SENDER_PERSONAL = "Restauranty";
    
    String ENCODING = "UTF-8";
    
    interface ReviewReport {
        
        String SUBJECT = "Restauranty - review report result";
        
        String POSITIVE_TEMPLATE = "reviewReportPositive.ftl";
        
        String NEGATIVE_TEMPLATE = "reviewReportNegative.ftl";
        
    }
    
    interface PasswordRecovery {
        
        String TEMPLATE = "passwordRecovery.ftl";
        
        String SUBJECT = "Restauranty - password recovery request";
        
    }
    
}
