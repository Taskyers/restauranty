package pl.taskyers.restauranty.repository.addresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.restauranty.core.data.addresses.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    Address findByStreetAndZipCodeAndCityAndCountry(String street, String zipCode, String city, String country);
    
}
