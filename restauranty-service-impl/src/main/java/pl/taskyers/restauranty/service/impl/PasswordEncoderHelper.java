package pl.taskyers.restauranty.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderHelper {
    
    private final PasswordEncoder passwordEncoder;
    
    public String getEncodedPassword(@NonNull String password) {
        return passwordEncoder.encode(password);
    }
    
}
