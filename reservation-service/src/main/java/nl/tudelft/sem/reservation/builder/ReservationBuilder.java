package nl.tudelft.sem.reservation.builder;

import java.time.LocalDateTime;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ReservationBuilder implements Builder {

    private Long madeBy;
    private Long roomId;
    private LocalDateTime start;
    private LocalDateTime end;
    private ReservationType reservationType;
    private Long userId;
    private Long groupId;
    private String purposeMessage;

    /**
     * Constructor for the Reservation class. Contains some mandatory values
     *
     * @param madeBy    a Long representing the user trying to make the
     *                  reservation the builder is for
     * @param roomId    a Long representing the room the reservation should be in
     * @param start     a LocalDateTime representing when the reservation should start
     * @param end       a LocalDateTime representing when the reservation should end
     */
    public ReservationBuilder(Long madeBy, Long roomId, LocalDateTime start, LocalDateTime end) {
        this.madeBy = madeBy;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
    }

    /**
     * Adds a given type to the builder, to be used when building the reservation.
     *
     * @param type  The type of reservation to be added (SELF, SINGLE, ADMIN, or GROUP)
     */
    public void type(ReservationType type) {
        this.reservationType = type;
    }

    /**
     * Adds a given user the reservation is for to the builder,
     * to be used when building the reservation.
     *
     * @param userId   A Long representing the user
     */
    public void user(Long userId) {
        this.userId = userId;
    }

    /**
     * Adds a given group the reservation is for to the builder,
     * to be used when building the reservation.
     *
     * @param groupId   A Long representing the group
     */
    public void group(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Adds a given purpose message to the builder, to be used when building the reservation.
     *
     * @param message   A string containing the purpose of the reservation
     */
    public void purpose(String message) {
        this.purposeMessage = message;
    }

    /**
     * Get madeBy.
     *
     * @return  a Long representing the user trying to make the reservation the builder is for
     */
    public Long getMadeBy() {
        return madeBy;
    }

    /**
     * Builds the reservation with the given values.
     *
     * @return  A Reservation object with the attributes specified with above methods
     */
    public Reservation build() {
        return new Reservation(madeBy, roomId, start, end,
                reservationType, userId, groupId, purposeMessage);
    }
}