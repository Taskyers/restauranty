package pl.taskyers.restauranty.core.data.addresses.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String street;
    
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String country;
    
}
