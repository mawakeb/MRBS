package nl.tudelft.sem.reservation.repository;


import nl.tudelft.sem.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Reservation Repository Interface.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByRoomIdInAndStartBeforeAndEndAfter(
            Iterable<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.roomId = :roomId AND ((r.start >= :start AND r.start < :end)" +
            "OR (:start >= r.start AND :start < r.end))")
    List<Reservation> findAllForASpecificRoomWithinGivenTimeRange(@Param("roomId") Long roomId,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    //find reservations for a user that overlap with the given time
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.userId = :userId AND ((r.start >= :start AND r.start < :end)" +
            "OR (:start >= r.start AND :start < r.end))")
    List<Reservation> findAllOverlappingWithAGivenReservationByUserId(@Param("userId") Long userId,
                                                                      @Param("start") LocalDateTime start,
                                                                      @Param("end") LocalDateTime end);

    //find reservations that overlap with the given time
    @Query("SELECT r FROM Reservation r " +
            "WHERE (r.start >= :start AND r.start < :end)" +
            "OR (:start >= r.start AND :start < r.end)")
    List<Reservation> findAllOverlapping(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

}
