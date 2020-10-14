package pl.taskyers.restauranty.email.service;

import lombok.NonNull;
import pl.taskyers.restauranty.email.service.dto.EmailAddresseeDTO;
import pl.taskyers.restauranty.email.service.enums.EmailType;

/**
 * Interface for sending email messages.
 *
 * @author Jakub Sildatk
 */
public interface EmailService {
    
    /**
     * Send single message to single addressee based on only email address
     *
     * @param email     email address as String
     * @param emailType {@link EmailType}
     * @param data      array of needed data for creating model as Strings
     * @since 1.0.0
     */
    void sendEmailToSingleAddressee(@NonNull String email, @NonNull EmailType emailType, @NonNull String... data);
    
    /**
     * Send single message to single addressee
     *
     * @param emailAddresseeDTO {@link EmailAddresseeDTO}
     * @param emailType         {@link EmailType}
     * @param data              array of needed data for creating model as Strings
     * @since 1.0.0
     */
    void sendEmailToSingleAddressee(@NonNull EmailAddresseeDTO emailAddresseeDTO, @NonNull EmailType emailType, @NonNull String... data);
    
}
