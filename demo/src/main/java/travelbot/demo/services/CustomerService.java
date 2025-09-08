package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.Customer;
import travelbot.demo.exceptions.BadRequestException;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.payloads.CustomerCreateRequest;
import travelbot.demo.payloads.CustomerUpdateRequest;
import travelbot.demo.repositories.CustomerRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

    public Optional<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public Customer create(CustomerCreateRequest req) {
        if (customerRepository.findByPhone(req.phone()).isPresent()) {
            throw new BadRequestException("Phone already in use: " + req.phone());
        }
        Customer c = new Customer(req.phone(), req.name());
        if (req.leadValue() != null) c.setLeadValue(req.leadValue());
        if (req.preferences() != null) c.setPreferences(req.preferences());
        return customerRepository.save(c);
    }

    public Customer update(Long id, CustomerUpdateRequest req) {
        Customer c = findById(id);
        if (req.name() != null) c.setName(req.name());
        if (req.leadValue() != null) c.setLeadValue(req.leadValue());
        if (req.preferences() != null) c.setPreferences(req.preferences());
        return customerRepository.save(c);
    }

    public void touchLastContact(Long customerId) {
        Customer c = findById(customerId);
        c.setLastContactedAt(Instant.now());
        customerRepository.save(c);
    }
}
