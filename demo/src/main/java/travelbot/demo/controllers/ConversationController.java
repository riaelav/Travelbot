package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.Conversation;
import travelbot.demo.payloads.ConversationCreateRequest;
import travelbot.demo.payloads.ConversationResponse;
import travelbot.demo.payloads.ConversationSummaryDTO;
import travelbot.demo.payloads.MessageResponse;
import travelbot.demo.repositories.ConversationRepository;
import travelbot.demo.repositories.MessageRepository;
import travelbot.demo.services.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationResponse start(@RequestBody ConversationCreateRequest payload) {
        Conversation conv = conversationService.start(payload.customerId());
        return new ConversationResponse(
                conv.getId(), conv.getCustomer().getId(),
                conv.getStartedAt(), conv.getClosedAt()
        );
    }

    @PostMapping("/{id}/close")
    public ConversationResponse close(@PathVariable Long id) {
        Conversation conv = conversationService.close(id);
        return new ConversationResponse(
                conv.getId(), conv.getCustomer().getId(),
                conv.getStartedAt(), conv.getClosedAt()
        );
    }

    
    @GetMapping
    public List<ConversationSummaryDTO> list() {
        return conversationRepository.findAllByOrderByStartedAtDesc()
                .stream()
                .map(c -> new ConversationSummaryDTO(
                        c.getId(),
                        c.getCustomer() != null ? c.getCustomer().getPhone() : null,
                        c.getStartedAt(),
                        c.getClosedAt(),
                        messageRepository.countByConversationId(c.getId()),
                        c.getLeadValue() // enum LeadValue
                ))
                .toList();
    }

    @GetMapping("/{id}/messages")
    public List<MessageResponse> messages(@PathVariable Long id) {
        return messageRepository.findAllByConversationIdOrderByCreatedAtAsc(id)
                .stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getConversation().getId(),
                        m.getRole(),
                        m.getBody(),
                        m.getCreatedAt()
                ))
                .toList();
    }
}
