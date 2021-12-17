package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.communication.GroupCommunication;
import java.util.List;

public class SecretariesCanOnlyReserveEditForTheirResearchMembers extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation) throws InvalidReservationException {

        ReservationType type = reservation.getType();
        if(type == ReservationType.SELF) return super.checkNext(reservation);
        if(!UserCommunication.getUserType().equals("SECRETARY")) return super.checkNext(reservation);

        if(type == ReservationType.ADMIN)
        {
            if(GroupCommunication.isSecretaryOfUser(UserCommunication.getUser(), reservation.getUserId())) return super.checkNext(reservation);
            throw new InvalidReservationException("Employee is not part of this secretary's research group");
        }

        if(type == ReservationType.GROUP)
        {
            if(GroupCommunication.isSecretaryOfGroup(UserCommunication.getUser(), reservation.getGroupId())) return super.checkNext(reservation);
            throw new InvalidReservationException("User is not secretary of this group");
        }

        throw new InvalidReservationException("No valid reservation type set");
    }
}
