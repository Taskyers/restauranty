package pl.taskyers.restauranty.email.service.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.restauranty.email.service.EmailService;
import pl.taskyers.restauranty.email.service.dto.EmailAddresseeDTO;
import pl.taskyers.restauranty.email.service.enums.EmailConstants;
import pl.taskyers.restauranty.email.service.enums.EmailType;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    private final it.ozimov.springboot.mail.service.EmailService emailService;
    
    @Override
    public void sendEmailToSingleAddressee(@NonNull final String email, @NonNull final EmailType emailType, @NonNull final String... data) {
        sendEmailToSingleAddressee(new EmailAddresseeDTO(email, "", ""), emailType, data);
    }
    
    @Override
    public void sendEmailToSingleAddressee(@NonNull final EmailAddresseeDTO emailAddresseeDTO, @NonNull final EmailType emailType,
            @NonNull final String... data) {
        if ( emailType == EmailType.PASSWORD_RECOVERY ) {
            sendPasswordRecoveryEmail(emailAddresseeDTO, data[0]);
        } else {
            log.warn(String.format("Email type: %s is not implemented. Message will not be sent", emailType.name()));
        }
    }
    
    private void sendPasswordRecoveryEmail(EmailAddresseeDTO emailAddresseeDTO, String token) {
        try {
            final Map<String, Object> model = createModel("token", token);
            final Email email = createEmail(emailAddresseeDTO.getEmail(), emailAddresseeDTO.getName() + " " + emailAddresseeDTO.getSurname(),
                    EmailConstants.PasswordRecovery.SUBJECT);
            emailService.send(email, EmailConstants.PasswordRecovery.TEMPLATE, model);
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            log.error("Could not sent an email", e);
        }
    }
    
    @VisibleForTesting
    Email createEmail(String address, String personal, String subject) throws UnsupportedEncodingException {
        return DefaultEmail.builder()
                .from(new InternetAddress(EmailConstants.SENDER_ADDRESS, EmailConstants.SENDER_PERSONAL))
                .to(Collections.singletonList(new InternetAddress(address, personal)))
                .subject(subject)
                .body("")
                .encoding(EmailConstants.ENCODING)
                .build();
    }
    
    @VisibleForTesting
    Map<String, Object> createModel(String key, Object value) {
        return createModel(Collections.singletonList(key), Collections.singletonList(value));
    }
    
    @VisibleForTesting
    Map<String, Object> createModel(List<String> keys, List<Object> values) {
        Map<String, Object> model = Maps.newHashMapWithExpectedSize(keys.size());
        for ( int i = 0; i < keys.size(); i++ ) {
            model.put(keys.get(i), values.get(i));
        }
        return model;
    }
    
}
