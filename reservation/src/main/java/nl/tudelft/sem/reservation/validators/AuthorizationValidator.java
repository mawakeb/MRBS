package nl.tudelft.sem.reservation.validators;

import com.google.gson.Gson;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public class AuthorizationValidator extends BaseValidator {

    protected static Gson gson = new Gson();

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        if (reservation.getType() == ReservationType.SELF) {
            super.checkNext(reservation, token);
        }
        if (UserCommunication.getUserType(token).equals("ADMIN")) {
            return super.checkNext(reservation, token);
        }

        if (reservation.getType() == ReservationType.ADMIN) {
            throw new InvalidReservationException("User is not an admin.");
        }

        if (UserCommunication.getUserType(token).equals("SECRETARY")) {
            return super.checkNext(reservation, token);
        }
        throw new InvalidReservationException("User is not a secretary.");
    }
}
