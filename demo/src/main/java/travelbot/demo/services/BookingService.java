package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.Booking;
import travelbot.demo.entities.Conversation;
import travelbot.demo.entities.Customer;
import travelbot.demo.enums.BookingStatus;
import travelbot.demo.exceptions.BadRequestException;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.payloads.BookingCreateRequest;
import travelbot.demo.payloads.BookingUpdateStatusRequest;
import travelbot.demo.repositories.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ConversationService conversationService;

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found: " + id));
    }

    public Booking create(BookingCreateRequest req) {
        Customer customer = customerService.findById(req.customerId());
        Conversation conversation = null;

        if (req.conversationId() != null) {
            conversation = conversationService.findById(req.conversationId());
            if (!conversation.getCustomer().getId().equals(customer.getId())) {
                throw new BadRequestException("Conversation does not belong to the customer");
            }
        }

        Booking b = new Booking(customer);
        b.setConversation(conversation);
        b.setProductSummary(req.productSummary());
        b.setPriceCents(req.priceCents());
        return bookingRepository.save(b);
    }

    public Booking updateStatus(Long id, BookingUpdateStatusRequest req) {
        Booking b = findById(id);
        b.setStatus(req.status());
        return bookingRepository.save(b);
    }

    public long countByStatus(BookingStatus status) {
        return bookingRepository.countByStatus(status);
    }
}
