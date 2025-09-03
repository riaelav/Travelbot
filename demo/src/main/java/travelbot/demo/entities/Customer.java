package travelbot.demo.entities;


import jakarta.persistence.*;
import travelbot.demo.enums.LeadValue;

import java.time.Instant;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "lead_value")
    private LeadValue leadValue;

    @Column(name = "client_preferences", columnDefinition = "jsonb")
    private String preferencesJson;

    @Column(name = "last_contact")
    private Instant lastContactedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Customer() {
    }


    public Customer(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    // Getters/Setters
    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LeadValue getLeadValue() {
        return leadValue;
    }

    public void setLeadValue(LeadValue leadValue) {
        this.leadValue = leadValue;
    }

    public String getPreferencesJson() {
        return preferencesJson;
    }

    public void setPreferencesJson(String preferencesJson) {
        this.preferencesJson = preferencesJson;
    }

    public Instant getLastContactedAt() {
        return lastContactedAt;
    }

    public void setLastContactedAt(Instant lastContactedAt) {
        this.lastContactedAt = lastContactedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "createdAt=" + createdAt +
                ", lastContactedAt=" + lastContactedAt +
                ", preferencesJson='" + preferencesJson + '\'' +
                ", leadValue=" + leadValue +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}