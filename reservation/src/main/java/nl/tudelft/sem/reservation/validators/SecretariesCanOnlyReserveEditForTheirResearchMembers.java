package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.communication.GroupCommunication;
import java.util.List;

public class SecretariesCanOnlyReserveEditForTheirResearchMembers extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token) throws InvalidReservationException {

        ReservationType type = reservation.getType();
        if(type == ReservationType.SELF) return super.checkNext(reservation, token);
        if(!UserCommunication.getUserType(token).equals("SECRETARY")) return super.checkNext(reservation, token);

        if(type == ReservationType.ADMIN)
        {
            if(GroupCommunication.isSecretaryOfUser(UserCommunication.getUser(token), reservation.getUserId(), token)) return super.checkNext(reservation, token);
            throw new InvalidReservationException("Employee is not part of this secretary's research group");
        }

        if(type == ReservationType.GROUP)
        {
            if(GroupCommunication.isSecretaryOfGroup(UserCommunication.getUser(token), reservation.getGroupId(), token)) return super.checkNext(reservation, token);
            throw new InvalidReservationException("User is not secretary of this group");
        }

        throw new InvalidReservationException("No valid reservation type set");
    }
}
