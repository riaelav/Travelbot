package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelbot.demo.entities.Conversation;
import travelbot.demo.entities.Message;
import travelbot.demo.enums.MessageRole;
import travelbot.demo.exceptions.NotFoundException;
import travelbot.demo.payloads.MessageCreateRequest;
import travelbot.demo.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private CustomerService customerService;

    public Message findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message not found: " + id));
    }

    public List<Message> listByConversation(Long conversationId) {
        return messageRepository.findAllByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    public Message create(MessageCreateRequest req) {
        Conversation conv = conversationService.findById(req.conversationId());
        Message msg = new Message(conv, req.role(), req.body());
        Message saved = messageRepository.save(msg);

        if (req.role() == MessageRole.USER) {
            customerService.touchLastContact(conv.getCustomer().getId());
        }
        return saved;
    }
}
