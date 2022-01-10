package nl.tudelft.sem.reservation.validators;

import java.time.LocalDateTime;
import java.util.List;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckIfRoomIsNotReservedAlready extends BaseValidator {

    @Autowired
    private transient ReservationRepository reservationRepo;

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        Long roomId = reservation.getRoomId();
        LocalDateTime reservationStart = reservation.getStart();
        LocalDateTime reservationEnd = reservation.getEnd();

        List<Reservation> overlappingReservationsOfUser =
                findAllForSpecificRoomWithinGivenTimeRange(roomId,
                        reservationStart, reservationEnd);

        if (!overlappingReservationsOfUser.isEmpty()) {
            throw new InvalidReservationException("There is another "
                    + "reservation overlapping with your desired time range.");
        }

        return super.checkNext(reservation, token);
    }


    /**
     * Find all the reservation for a given room in a given time frame.
     *
     * @param roomId the id of the room
     * @param start the start time of the time frame
     * @param end the end time of the time frame
     * @return a list of the reservation in that room in the given time frame
     */
    public List<Reservation> findAllForSpecificRoomWithinGivenTimeRange(long roomId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end) {
        return reservationRepo.findAllForSpecificRoomWithinGivenTimeRange(roomId,
                start, end);
    }
}
