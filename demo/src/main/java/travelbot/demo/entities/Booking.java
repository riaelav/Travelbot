package travelbot.demo.entities;

import jakarta.persistence.*;
import travelbot.demo.enums.BookingStatus;

import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(columnDefinition = "text", name = "product_summary")
    private String productSummary;
    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "booking_status")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, name = "created_at")
    private Instant createdAt;

    public Booking() {
    }

    public Booking(Customer customer) {
        this.customer = customer;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    public Integer getPriceCents() {
        return price;
    }

    public void setPriceCents(Integer priceCents) {
        this.price = priceCents;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer=" + customer +
                ", conversation=" + conversation +
                ", productSummary='" + productSummary + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
