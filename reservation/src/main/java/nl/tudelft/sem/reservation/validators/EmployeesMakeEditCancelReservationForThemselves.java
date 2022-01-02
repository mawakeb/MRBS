package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.communication.GroupCommunication;

public class EmployeesMakeEditCancelReservationForThemselves extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        String userType = UserCommunication.getUserType();
        if(userType.equals("ADMIN")) return super.checkNext(reservation);

        if(userType.equals("EMPLOYEE"))
        {
            if(UserCommunication.getUser().equals(reservation.getMadeBy())) return super.checkNext(reservation);
            throw new InvalidReservationException("Employees cannot manage reservations for someone else");
        }

        if(userType.equals("SECRETARY"))
        {
            if(GroupCommunication.isSecretaryOfUser(reservation.getMadeBy(), UserCommunication.getUser())) return super.checkNext(reservation);
            throw new InvalidReservationException("The given employee is not in your research group");
        }

        throw new InvalidReservationException("Invalid user");
    }
}
