package nl.tudelft.sem.reservation.communication;

import java.util.List;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


public class UserController {


    private final transient ReservationRepository reservationRepo;

    @Autowired
    public UserController(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    /**
     * Check if a given user made a certain reservation.
     *
     * @param madeBy the id of the user
     * @param reservationId the id of the reservation
     * @return if the user made the reservation
     */
    @GetMapping("/checkUser")
    public boolean checkUser(@RequestParam long madeBy, @RequestParam long reservationId) {

        Reservation reservation = reservationRepo.findById(reservationId).orElse(null);
        if (reservation != null) {
            return reservation.getMadeBy().equals(madeBy);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION_NOT_FOUND");
        }
    }

    /**
     * Gets the schedule of a certain user from the database.
     *
     * @param userId - the id of the user
     * @return - a List of all the reservations they made
     */
    @GetMapping("getSchedule")
    public List<Reservation> getSchedule(@RequestParam long userId) {
        return reservationRepo.findByUserId(userId);
    }
}
