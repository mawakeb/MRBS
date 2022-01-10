package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;


public class EmployeesMakeEditCancelReservationForThemselves
        extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        if (!getUserType(token).equals("EMPLOYEE")) {
            return super.checkNext(reservation, token);
        }

        if (reservation.getMadeBy().equals(reservation.getUserId())) {
            return super.checkNext(reservation, token);
        }
        throw new InvalidReservationException("Employees cannot manage reservations "
                + "for someone else.");
    }

    /**
     * Get the type of user .
     * Added to allow unit testing.
     *
     * @param token the authentication token of the current user
     * @return the type of the user
     */
    public String getUserType(String token) {
        return UserCommunication.getUserType(token);
    }
}
