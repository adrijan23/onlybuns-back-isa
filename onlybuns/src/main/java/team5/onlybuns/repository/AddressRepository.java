package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.onlybuns.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByStreetAndStreetNumber(String street, String streetNumber);
}
