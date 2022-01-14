package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.GroupCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;

public class SecretariesCanOnlyReserveEditForTheirResearchMembers extends BaseValidator {

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        ReservationType type = reservation.getType();

        if (type.equals(ReservationType.SINGLE)) {
            return handleSingleReservation(reservation, token);
        }

        if (type.equals(ReservationType.GROUP)) {
            return handleGroupReservation(reservation, token);
        }

        return super.checkNext(reservation, token);
    }

    private boolean handleSingleReservation(Reservation reservation, String token)
            throws InvalidReservationException {

        Long groupId = reservation.getGroupId();
        if (isSecretaryOfGroup(reservation.getMadeBy(),
                groupId, token)
                && isInGroup(reservation.getUserId(),
                groupId, token)) {
            return super.checkNext(reservation, token);
        }
        throw new InvalidReservationException("Employee "
                    + "is not part of this secretary's research group.");
    }

    private boolean handleGroupReservation(Reservation reservation, String token)
            throws InvalidReservationException {

        if (isSecretaryOfGroup(reservation.getMadeBy(),
                reservation.getGroupId(), token)) {
            return super.checkNext(reservation, token);
        }
        throw new InvalidReservationException("User is not secretary of this group.");
    }

    /**
     * Check if the person making the reservation is the secretary of a group.
     *
     * @param madeBy  the id of the person who made the reservation
     * @param groupId the group given with the reservation
     * @param token   the authentication token of the current user
     * @return if the person making the reservation is the secretary of the group
     */
    public boolean isSecretaryOfGroup(long madeBy, long groupId, String token) {
        return GroupCommunication.isSecretaryOfGroup(madeBy, groupId, token);
    }

    /**
     * Check if the person who the reservation is for is in the group.
     *
     * @param userId  the id of the person who made the reservation
     * @param groupId the group given with the reservation
     * @param token   the authentication token of the current user
     * @return if the person who the reservation is for is in the group
     */
    public boolean isInGroup(long userId, long groupId, String token) {
        return GroupCommunication.isInGroup(userId, groupId, token);
    }
}
