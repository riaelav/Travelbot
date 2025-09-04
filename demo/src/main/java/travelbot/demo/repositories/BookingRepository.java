package travelbot.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import travelbot.demo.entities.Booking;
import travelbot.demo.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByStatus(BookingStatus status);

    Page<Booking> findByCustomerId(Long customerId, Pageable pageable);
}
