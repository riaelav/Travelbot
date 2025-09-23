package travelbot.demo.entities;

import jakarta.persistence.*;
import travelbot.demo.enums.LeadValue;

import java.time.Instant;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, name = "started_at")
    private Instant startedAt;
    @Column(name = "closed_at")
    private Instant closedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadValue leadValue = LeadValue.LOW;

    public Conversation() {
    }

    public Conversation(Customer customer) {
        this.customer = customer;
    }

    @PrePersist
    public void prePersist() {
        if (this.startedAt == null) this.startedAt = Instant.now();
    }

    // Getters/Setters
    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public LeadValue getLeadValue() {
        return leadValue;
    }

    public void setLeadValue(LeadValue leadValue) {
        this.leadValue = leadValue;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", customer=" + customer +
                ", startedAt=" + startedAt +
                ", closedAt=" + closedAt +
                '}';
    }
}
