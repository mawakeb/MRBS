package nl.tudelft.sem.reservation.repository;


import nl.tudelft.sem.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Reservation Repository Interface.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByRoomIdAndStartIsBeforeAndEndIsAfter(
            Iterable<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime);
}
