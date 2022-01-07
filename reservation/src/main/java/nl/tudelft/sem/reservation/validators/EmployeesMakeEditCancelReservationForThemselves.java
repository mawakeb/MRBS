package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.communication.GroupCommunication;

public class EmployeesMakeEditCancelReservationForThemselves extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token) throws InvalidReservationException {

        String userType = UserCommunication.getUserType(token);
        if(userType.equals("ADMIN")) return super.checkNext(reservation, token);

        if(userType.equals("EMPLOYEE"))
        {
            if(UserCommunication.getUser(token).equals(reservation.getMadeBy())) return super.checkNext(reservation, token);
            throw new InvalidReservationException("Employees cannot manage reservations for someone else");
        }

        if(userType.equals("SECRETARY"))
        {
            if(GroupCommunication.isSecretaryOfUser(reservation.getMadeBy(), UserCommunication.getUser(token), token)) return super.checkNext(reservation, token);
            throw new InvalidReservationException("The given employee is not in your research group");
        }

        throw new InvalidReservationException("Invalid user");
    }
}
