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
            checkForOverlapsInCaseOfGroupReservation(reservation, token);
        } else {
            Long userId = reservation.getUserId();
            LocalDateTime reservationStart = reservation.getStart();
            LocalDateTime reservationEnd = reservation.getEnd();

            List<Reservation> overlappingReservationsOfUser =
                    findAllOverlappingWithGivenReservationByUserId(userId,
                            reservationStart, reservationEnd);

            if (!overlappingReservationsOfUser.isEmpty()) {
                throw new InvalidReservationException("Users can only have one reservation"
                        + " within a given time range.");
            }
        }

        return super.checkNext(reservation, token);
    }

    /**
     * Checks if there are any overlapping existing reservations with your GROUP reservation.
     * If there are, it returns the corresponding error message -
     * if there is another group reservation with which it overlaps
     * or if someone from your group has another reservation during that time slot.
     *
     * @param reservation - reservation that you want to make.
     * @param token - the authentication token of the current user.
     * @throws InvalidReservationException - indicating that the reservation cannot be made.
     */
    private void checkForOverlapsInCaseOfGroupReservation(Reservation reservation, String token)
            throws InvalidReservationException {
        List<Reservation> reservationsAtThisTime = findAllOverlapping(reservation.getStart(),
                reservation.getEnd());

        for (Reservation conflict : reservationsAtThisTime) {
            //For each reservation in the list, check if another group already
            // has a reservation within the desired time to book.
            if (conflict.getType().equals(ReservationType.GROUP)) {
                if (overlap(reservation.getGroupId(), conflict.getGroupId(), token)) {
                    throw new InvalidReservationException("Group reservation "
                            + "conflicts with another group.");
                }
            } else {
                // If not, check if a member of your research group doesn't already
                // have a reservation within the desired time to book.
                if (isInGroup(conflict.getUserId(), reservation.getGroupId(), token)) {
                    throw new InvalidReservationException("Not all group members are "
                            + "available at the given time.");
                }
            }
        }
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
        return reservationRepo.findByUserIdAndStartBeforeAndEndAfterAndCancelledIsFalse(userId,
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
        return reservationRepo.findByStartBeforeAndEndAfterAndCancelledIsFalse(start, end);
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
