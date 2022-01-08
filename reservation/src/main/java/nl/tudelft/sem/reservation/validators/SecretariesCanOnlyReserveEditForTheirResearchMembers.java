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
        if(type.equals(ReservationType.SELF) || type.equals(ReservationType.ADMIN)) return super.checkNext(reservation, token);

        if(type.equals(ReservationType.SINGLE))
        {
            if(GroupCommunication.isSecretaryOfGroup(reservation.getMadeBy(), reservation.getGroupId(), token)
            && GroupCommunication.isInGroup(reservation.getUserId(), reservation.getGroupId(), token))
                return super.checkNext(reservation, token);
            throw new InvalidReservationException("Employee is not part of this secretary's research group.");
        }

        if(type.equals(ReservationType.GROUP))
        {
            if(GroupCommunication.isSecretaryOfGroup(reservation.getMadeBy(), reservation.getGroupId(), token)) return super.checkNext(reservation, token);
            throw new InvalidReservationException("User is not secretary of this group.");
        }

        throw new InvalidReservationException("No valid reservation type set.");
    }
}
