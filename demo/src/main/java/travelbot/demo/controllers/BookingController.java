package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.Booking;
import travelbot.demo.exceptions.ValidationException;
import travelbot.demo.payloads.BookingCreateRequest;
import travelbot.demo.payloads.BookingResponse;
import travelbot.demo.payloads.BookingUpdateStatusRequest;
import travelbot.demo.services.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // CREATE booking
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody @Validated BookingCreateRequest payload,
                                  BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors()
                            .stream().map(field -> field.getDefaultMessage()).toList()
            );
        }
        Booking b = bookingService.create(payload);
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(),
                b.getConversation() != null ? b.getConversation().getId() : null,
                b.getProductSummary(),
                b.getPriceCents(),
                b.getStatus(),
                b.getCreatedAt()
        );
    }

    // READ all bookings
    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.findAll().stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getCustomer().getId(),
                        b.getConversation() != null ? b.getConversation().getId() : null,
                        b.getProductSummary(),
                        b.getPriceCents(),
                        b.getStatus(),
                        b.getCreatedAt()
                ))
                .toList();
    }

    // READ single booking by ID
    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        Booking b = bookingService.findById(id);
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(),
                b.getConversation() != null ? b.getConversation().getId() : null,
                b.getProductSummary(),
                b.getPriceCents(),
                b.getStatus(),
                b.getCreatedAt()
        );
    }

    // READ bookings of a specific customer
    @GetMapping("/customer/{customerId}")
    public List<BookingResponse> getByCustomer(@PathVariable Long customerId) {
        return bookingService.findByCustomer(customerId).stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getCustomer().getId(),
                        b.getConversation() != null ? b.getConversation().getId() : null,
                        b.getProductSummary(),
                        b.getPriceCents(),
                        b.getStatus(),
                        b.getCreatedAt()
                ))
                .toList();
    }

    // UPDATE booking status
    @PutMapping("/{id}/status")
    public BookingResponse updateStatus(@PathVariable Long id,
                                        @RequestBody @Validated BookingUpdateStatusRequest payload,
                                        BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors()
                            .stream().map(field -> field.getDefaultMessage()).toList()
            );
        }
        // passo il DTO intero, non solo lo status
        Booking b = bookingService.updateStatus(id, payload);
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(),
                b.getConversation() != null ? b.getConversation().getId() : null,
                b.getProductSummary(),
                b.getPriceCents(),
                b.getStatus(),
                b.getCreatedAt()
        );
    }

}
