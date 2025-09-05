package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.Conversation;
import travelbot.demo.payloads.ConversationCreateRequest;
import travelbot.demo.payloads.ConversationResponse;
import travelbot.demo.services.ConversationService;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

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
}
