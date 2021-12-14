package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.ReservationController;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class EmployeesOneReservationDuringTimeSlot extends BaseValidator {

    @Autowired
    private transient ReservationRepository reservationRepo;

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        String userType = UserCommunication.getUserType();
        if (!userType.equals("EMPLOYEE")) {
            return super.checkNext(reservation);
        }

        Long userId = reservation.getUserId();
        LocalDateTime reservationStart = reservation.getStart();
        LocalDateTime reservationEnd = reservation.getEnd();

        List<Reservation> overlappingReservationsOfUser = reservationRepo.findAllOverlappingWithAGivenReservationByUserId
                                                                    (userId, reservationStart, reservationEnd);

        if (!overlappingReservationsOfUser.isEmpty()) {
            throw new InvalidReservationException("Employees can only have one reservation within a given time range.");
        }

        return super.checkNext(reservation);
    }
}