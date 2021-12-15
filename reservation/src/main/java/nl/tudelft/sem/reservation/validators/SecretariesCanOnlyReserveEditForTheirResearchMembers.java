package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import java.util.List;

public class SecretariesCanOnlyReserveEditForTheirResearchMembers extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        String userType = UserCommunication.getUserType();
        if(!userType.equals("SECRETARY")) return super.checkNext(reservation);

        Long userID = UserCommunication.getUser();
        List groupMembers = UserCommunication.getTeamMembers(userID);
        //Secretaries are assumed to be in their own group
        if(groupMembers.contains(reservation.getUserId())) return super.checkNext(reservation);

        throw new InvalidReservationException("Employee is not part of this secretary's research group");
    }
}
