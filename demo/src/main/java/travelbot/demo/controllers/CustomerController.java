package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.Customer;
import travelbot.demo.exceptions.ValidationException;
import travelbot.demo.payloads.CustomerCreateRequest;
import travelbot.demo.payloads.CustomerResponse;
import travelbot.demo.payloads.CustomerUpdateRequest;
import travelbot.demo.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@RequestBody @Validated CustomerCreateRequest payload,
                                   BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors()
                            .stream().map(field -> field.getDefaultMessage()).toList()
            );
        }
        Customer c = customerService.create(payload);
        return new CustomerResponse(
                c.getId(), c.getPhone(), c.getName(),
                c.getLeadValue() != null ? c.getLeadValue().name() : null,
                c.getPreferences(), c.getLastContactedAt(), c.getCreatedAt()
        );
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable Long id) {
        Customer c = customerService.findById(id);
        return new CustomerResponse(
                c.getId(), c.getPhone(), c.getName(),
                c.getLeadValue() != null ? c.getLeadValue().name() : null,
                c.getPreferences(), c.getLastContactedAt(), c.getCreatedAt()
        );
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id,
                                   @RequestBody @Validated CustomerUpdateRequest payload,
                                   BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(field -> field.getDefaultMessage()).toList());
        }
        Customer c = customerService.update(id, payload);
        return new CustomerResponse(
                c.getId(), c.getPhone(), c.getName(),
                c.getLeadValue() != null ? c.getLeadValue().name() : null,
                c.getPreferences(), c.getLastContactedAt(), c.getCreatedAt()
        );
    }
}
