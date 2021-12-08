package nl.tudelft.sem.reservation.repository;


import nl.tudelft.sem.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The User Repository Interface.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAll();
}
