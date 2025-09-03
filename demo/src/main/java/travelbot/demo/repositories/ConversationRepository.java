package travelbot.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbot.demo.entities.Conversation;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findTopByCustomerIdOrderByStartedAtDesc(Long customerId);
}
