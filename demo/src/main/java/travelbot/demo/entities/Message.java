package travelbot.demo.entities;

import jakarta.persistence.*;
import travelbot.demo.enums.MessageRole;

import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageRole role;

    @Column(nullable = false, columnDefinition = "text")
    private String body;

    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    public Message() {
    }

    public Message(Conversation conversation, MessageRole role, String body) {
        this.conversation = conversation;
        this.role = role;
        this.body = body;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    // Getters/Setters
    public Long getId() {
        return id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public MessageRole getRole() {
        return role;
    }

    public void setRole(MessageRole role) {
        this.role = role;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", conversation=" + conversation +
                ", role=" + role +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
