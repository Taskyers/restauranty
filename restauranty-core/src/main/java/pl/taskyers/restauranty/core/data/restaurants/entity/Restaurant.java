package pl.taskyers.restauranty.core.data.restaurants.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;

import javax.persistence.*;

@Entity
@Table(name = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToOne(targetEntity = Address.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "address_id")
    private Address address;
    
    @Column(name = "phone_number", nullable = false, unique = true, length = 9)
    private String phoneNumber;
    
}
