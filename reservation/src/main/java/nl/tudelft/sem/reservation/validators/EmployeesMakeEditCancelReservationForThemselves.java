package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.communication.GroupCommunication;

public class EmployeesMakeEditCancelReservationForThemselves extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token) throws InvalidReservationException {

        if(UserCommunication.getUserType(token).equals("EMPLOYEE")) return super.checkNext(reservation, token);

        if(reservation.getMadeBy().equals(reservation.getUserId())) return super.checkNext(reservation, token);
        throw new InvalidReservationException("Employees cannot manage reservations for someone else.");
    }
}
