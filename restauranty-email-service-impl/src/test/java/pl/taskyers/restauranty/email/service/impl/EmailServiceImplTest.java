package pl.taskyers.restauranty.email.service.impl;

import it.ozimov.springboot.mail.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.taskyers.restauranty.email.service.dto.EmailAddresseeDTO;
import pl.taskyers.restauranty.email.service.enums.EmailConstants;
import pl.taskyers.restauranty.email.service.enums.EmailType;

import java.util.Map;

import static org.mockito.Mockito.*;

public class EmailServiceImplTest {
    
    private EmailServiceImpl emailService;
    
    private it.ozimov.springboot.mail.service.EmailService externalService;
    
    @BeforeEach
    public void setUp() {
        externalService = mock(it.ozimov.springboot.mail.service.EmailService.class);
        emailService = new EmailServiceImpl(externalService);
    }
    
    @Test
    public void testSendingPasswordRecoveryEmail() throws Exception {
        // given
        final String token = "test";
        final EmailAddresseeDTO emailAddresseeDTO = new EmailAddresseeDTO("test@email.com", "test", "test");
        final Email email = emailService.createEmail(emailAddresseeDTO.getEmail(), emailAddresseeDTO.getName() + " " + emailAddresseeDTO.getSurname(),
                EmailConstants.PasswordRecovery.SUBJECT);
        final Map<String, Object> model = emailService.createModel("token", token);
        
        // when
        emailService.sendEmailToSingleAddressee(emailAddresseeDTO, EmailType.PASSWORD_RECOVERY, token);
        
        // then
        verify(externalService).send(email, EmailConstants.PasswordRecovery.TEMPLATE, model);
    }
    
}