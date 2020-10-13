package pl.taskyers.restauranty.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "pl.taskyers.restauranty")
@EnableJpaRepositories(basePackages = "pl.taskyers.restauranty.repository")
@EntityScan(basePackages = "pl.taskyers.restauranty.core.data")
public class RestaurantyWarApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RestaurantyWarApplication.class, args);
    }
    
}
