package nl.tudelft.sem.reservation.validators;

import java.time.LocalDateTime;
import java.util.List;
import nl.tudelft.sem.reservation.communication.GroupCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;



public class EmployeesOneReservationDuringTimeSlot extends BaseValidator {

    @Autowired
    private transient ReservationRepository reservationRepo;

    @Override
    public boolean handle(Reservation reservation, String token)
            throws InvalidReservationException {

        if (reservation.getType().equals(ReservationType.GROUP)) {
            List<Reservation> reservationsAtThisTime = findAllOverlapping(reservation.getStart(),
                    reservation.getEnd());

            //For each reservation in the list, check if the associated user is in the group
            for (Reservation conflict : reservationsAtThisTime) {
                if (conflict.getType().equals(ReservationType.GROUP)) {
                    if (overlap(reservation.getGroupId(),
                            conflict.getGroupId(), token)) {
                        throw new InvalidReservationException("Group reservation "
                                + "conflicts with another group.");
                    }
                } else {
                    Long userId = conflict.getUserId();
                    if (isInGroup(userId, reservation.getGroupId(), token)) {
                        throw new InvalidReservationException("Not all group members are "
                                + "available at the given time.");
                    }
                }
            }
        } else {
            Long userId = reservation.getUserId();
            LocalDateTime reservationStart = reservation.getStart();
            LocalDateTime reservationEnd = reservation.getEnd();

            List<Reservation> overlappingReservationsOfUser =
                    findAllOverlappingWithGivenReservationByUserId(userId,
                            reservationStart, reservationEnd);

            if (!overlappingReservationsOfUser.isEmpty()) {
                throw new InvalidReservationException("Users can only have one "
                        + " within a given time range.");
            }
        }

        return super.checkNext(reservation, token);
    }


    /**
     * Find all the reservations for a given user in a given time frame.
     *
     * @param userId the id of the user
     * @param start the start time of the time frame
     * @param end the end time of the time frame
     * @return a list of the reservation for that user in the given time frame
     */
    public List<Reservation> findAllOverlappingWithGivenReservationByUserId(Long userId,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end) {
        return reservationRepo.findAllOverlappingWithGivenReservationByUserId(userId,
                start, end);
    }

    /**
     * Find all the reservations in a given time frame.
     *
     * @param start the start time of the time frame
     * @param end the end time of the time frame
     * @return a list of the reservations in the given time frame
     */
    public List<Reservation> findAllOverlapping(LocalDateTime start, LocalDateTime end) {
        return reservationRepo.findAllOverlapping(start, end);
    }

    /**
     * Check if the person who the reservation is for is in the group.
     *
     * @param userId  the id of the person who made the reservation
     * @param groupId the group given with the reservation
     * @param token   the authentication token of the current user
     * @return if the person who the reservation is for is in the group
     */
    public boolean isInGroup(Long userId, Long groupId, String token) {
        return GroupCommunication.isInGroup(userId, groupId, token);
    }

    /**
     * Check if 2 groups have overlapping members.
     *
     * @param group1Id the id of group 1
     * @param group2Id the id of group 2
     * @param token   the authentication token of the current user
     * @return if there is overlap between the groups
     */
    public boolean overlap(Long group1Id, Long group2Id, String token) {
        return GroupCommunication.isInGroup(group1Id, group2Id, token);
    }
}
