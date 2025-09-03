package travelbot.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbot.demo.entities.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByConversationIdOrderByCreatedAtAsc(Long conversationId);
}
