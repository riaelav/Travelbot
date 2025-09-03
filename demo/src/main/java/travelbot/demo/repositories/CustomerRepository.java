package travelbot.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbot.demo.entities.Customer;
import travelbot.demo.enums.LeadValue;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String phone);

    long countByLeadValue(LeadValue value);
}
