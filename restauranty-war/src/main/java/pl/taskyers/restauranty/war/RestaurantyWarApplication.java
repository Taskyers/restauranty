package pl.taskyers.restauranty.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "pl.taskyers.restauranty")
public class RestaurantyWarApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RestaurantyWarApplication.class, args);
    }
    
}
