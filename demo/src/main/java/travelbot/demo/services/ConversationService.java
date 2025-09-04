package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.Conversation;
import travelbot.demo.entities.Customer;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.repositories.ConversationRepository;

import java.time.Instant;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private CustomerService customerService;

    public Conversation findById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + id));
    }

    public Conversation start(Long customerId) {
        Customer c = customerService.findById(customerId);
        Conversation conv = new Conversation(c);
        return conversationRepository.save(conv);
    }

    public Conversation getLastForCustomer(Long customerId) {
        return conversationRepository.findTopByCustomerIdOrderByStartedAtDesc(customerId)
                .orElseThrow(() -> new NotFoundException("No conversations for customer: " + customerId));
    }

    public Conversation close(Long conversationId) {
        Conversation conv = findById(conversationId);
        if (conv.getClosedAt() == null) conv.setClosedAt(Instant.now());
        return conversationRepository.save(conv);
    }
}
