package nl.tudelft.sem.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Reservation Repository Interface.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /** Saves a reservation.
     *
     * @param reservation the reservation
     * @return the reservation
     */
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByRoomIdInAndStartBeforeAndEndAfterAndCancelledIsFalse(
            Iterable<Long> roomIds, LocalDateTime endTime, LocalDateTime startTime);

    List<Reservation> findByRoomIdAndStartBeforeAndEndAfterAndCancelledIsFalse(
            Long roomId, LocalDateTime endTime, LocalDateTime startTime);

    List<Reservation> findByUserIdAndStartBeforeAndEndAfterAndCancelledIsFalse(
            Long userId, LocalDateTime endTime, LocalDateTime startTime);

    List<Reservation> findByStartBeforeAndEndAfterAndCancelledIsFalse(
            LocalDateTime endTime, LocalDateTime startTime);
}
