package pl.taskyers.restauranty.auth.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static pl.taskyers.restauranty.auth.enums.SecurityConstants.*;

/**
 * Filter for JWT Authentication. Creates {@link UserAuthDTO} from JSON request containing username and password and returns {@link UsernamePasswordAuthenticationToken} based on provided credentials. <br>
 * On successful authentication JWT token is created and token is included in response header.
 *
 * @author Jakub Sildatk
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final UserAuthDTO credentials = new ObjectMapper().readValue(request.getInputStream(), UserAuthDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.addHeader(HEADER_ROLE, getRole(authResult.getAuthorities()));
    }
    
    @Getter
    private static final class UserAuthDTO {
        
        private String username;
        
        private String password;
        
    }
    
    private String getRole(Collection<? extends GrantedAuthority> authorities) {
        return Iterables.getFirst(authorities, "")
                .toString();
    }
    
}