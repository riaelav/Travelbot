package travelbot.demo.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import travelbot.demo.enums.LeadValue;

import java.time.Instant;
import java.util.Map;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "client_preferences", columnDefinition = "jsonb") //se salvo in json potrò fare delle query in futuro
    private Map<String, Object> preferences;


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

    public Map<String, Object> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Object> preferences) {
        this.preferences = preferences;
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

}