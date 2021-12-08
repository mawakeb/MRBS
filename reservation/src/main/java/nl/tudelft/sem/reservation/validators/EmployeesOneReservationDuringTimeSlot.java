package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public class EmployeesOneReservationDuringTimeSlot extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        String type = UserCommunication.getUserType();
        if (!type.equals("EMPLOYEE")) {
            return super.checkNext(reservation);
        }

        //TODO: check in database if a user has another reservation within the same time slot

        return super.checkNext(reservation);
    }
}
