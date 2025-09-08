package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.entities.Message;
import travelbot.demo.exceptions.ValidationException;
import travelbot.demo.payloads.MessageCreateRequest;
import travelbot.demo.payloads.MessageResponse;
import travelbot.demo.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse create(@RequestBody @Validated MessageCreateRequest payload,
                                  BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(
                    validationResult.getFieldErrors()
                            .stream().map(field -> field.getDefaultMessage()).toList()
            );
        }
        Message m = messageService.create(payload);
        return new MessageResponse(
                m.getId(), m.getConversation().getId(),
                m.getRole(), m.getBody(), m.getCreatedAt()
        );
    }

    @GetMapping("/conversation/{conversationId}")
    public List<MessageResponse> listByConversation(@PathVariable Long conversationId) {
        return messageService.listByConversation(conversationId)
                .stream()
                .map(m -> new MessageResponse(
                        m.getId(), m.getConversation().getId(),
                        m.getRole(), m.getBody(), m.getCreatedAt()
                ))
                .toList();
    }
}
