package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;

public class EmployeesMakeEditCancelReservationForThemselves extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        String userType = UserCommunication.getUserType();
        if(!userType.equals("EMPLOYEE")) return super.checkNext(reservation);

        Long userID = UserCommunication.getUser();
        if(userID.equals(reservation.getMadeBy())) return super.checkNext(reservation);

        throw new InvalidReservationException("Employees cannot manage reservations for someone else");
    }
}
