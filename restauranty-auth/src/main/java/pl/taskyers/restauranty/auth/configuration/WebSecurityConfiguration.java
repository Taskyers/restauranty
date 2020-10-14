package pl.taskyers.restauranty.auth.configuration;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.taskyers.restauranty.auth.filter.JwtAuthenticationFilter;
import pl.taskyers.restauranty.auth.filter.JwtAuthorizationFilter;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private final UserDetailsService userDetailsService;
    
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    
    @PostConstruct
    public void init() throws Exception {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(), userDetailsService);
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(ImmutableList.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(ImmutableList.of("*"));
        
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register/**", "/recovery/**")
                .permitAll()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers("/restaurant/**")
                .hasRole("RESTAURANT")
                .antMatchers("/client/**")
                .hasRole("CLIENT")
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(jwtAuthorizationFilter)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
}