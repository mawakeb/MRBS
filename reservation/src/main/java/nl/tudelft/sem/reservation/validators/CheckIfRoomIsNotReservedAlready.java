package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class CheckIfRoomIsNotReservedAlready extends BaseValidator {

    @Autowired
    private ReservationRepository reservationRepo;

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        Long userId = reservation.getUserId();
        LocalDateTime reservationStart = reservation.getStart();
        LocalDateTime reservationEnd = reservation.getEnd();

        List<Reservation> overlappingReservationsOfUser = reservationRepo.findAllForASpecificRoomWithinGivenTimeRange
                (userId, reservationStart, reservationEnd);

        if (!overlappingReservationsOfUser.isEmpty()) {
            throw new InvalidReservationException("There is another reservation overlapping with your desired time range.");
        }

        return super.checkNext(reservation);
    }
}
