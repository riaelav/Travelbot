package travelbot.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import travelbot.demo.entities.Booking;
import travelbot.demo.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByStatus(BookingStatus status);
}
