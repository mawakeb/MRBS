package nl.tudelft.sem.reservation.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    private transient Long madeBy;

    @Column(name = "roomId")
    private transient Long roomId;

    @Column(name = "start")
    private transient LocalDateTime start;

    @Column(name = "end")
    private transient LocalDateTime end;

    private transient ReservationType type;

    private transient Long userId;

    private transient Long groupId;

    @Column(name = "purpose")
    private transient String purpose;

    @Column(name = "editPurpose")
    private transient String editPurpose;

    @Column(name = "cancelled")
    private transient boolean cancelled;

    /**
     * Constructor for the Reservation class.
     *
     * @param madeBy  ID of the user who made the reservation.
     * @param roomId  Location of the reservation.
     * @param start   Date and time of the start of the reservation.
     * @param end     Date and time of the end of the reservation.
     * @param type    Whether this reservation was made for the user themselves,
     *                another user or a research group.
     * @param userId  The ID of the person the reservation was made for.
     * @param groupId The ID of the research group the reservation was made for.
     * @param purpose purpose of the reservation
     */
    public Reservation(Long madeBy, Long roomId, LocalDateTime start,
                       LocalDateTime end, ReservationType type,
                       Long userId, Long groupId, String purpose) {
        this.madeBy = madeBy;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
        this.type = type;
        this.userId = userId;
        this.groupId = groupId;
        this.purpose = purpose;
        this.cancelled = false;
    }

    /**
     * Get the id.
     *
     * @return  The ID of this reservation
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id    The new ID of this reservation
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the roomId.
     *
     * @return  A Long representing the room this reservation is in
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * Get madeBy.
     *
     * @return  A Long representing the user who made this reservation
     */
    public Long getMadeBy() {
        return madeBy;
    }

    /**
     * Get the start time.
     *
     * @return  A LocalDateTime representing the time this reservation starts or has started
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Get the end time.
     *
     * @return  A LocalDateTime representing the time this reservation ends or has ended
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Get the reservationPurpose.
     *
     * @return  A String describing why this reservation was made
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Get the edit purpose.
     *
     * @return  A String describing why this reservation was changed
     */
    public String getEditPurpose() {
        return editPurpose;
    }

    /**
     * Get the reservation type.
     *
     * @return  A ReservationType representing what kind of reservation this is
     */
    public ReservationType getType() {
        return type;
    }

    /**
     * Get the userId.
     *
     * @return  A Long representing the user this reservation was made for
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Get the groupId.
     *
     * @return  A Long representing the group this reservation was made for
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * Get the cancelled.
     *
     * @return A boolean representing if this reservation has been cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Changes the location of the reservation.
     *
     * @param newRoomId     A Long representing the new room the reservation should be set in
     * @param editPurpose   A String explaining why this reservation was moved
     */
    public void changeLocation(Long newRoomId, String editPurpose) {
        this.roomId = newRoomId;
        this.editPurpose = editPurpose;
    }

    /**
     * Changes the date and/or time of the reservation.
     *
     * @param newStart      A LocalDateTime representing when the reservation should start
     * @param newEnd        A LocalDateTime representing when the reservation should end
     * @param editPurpose   A String explaining why this reservation was rescheduled
     */
    public void changeTime(LocalDateTime newStart, LocalDateTime newEnd, String editPurpose) {
        this.start = newStart;
        this.end = newEnd;
        this.editPurpose = editPurpose;
    }

    /**
     * Cancels the reservation.
     *
     * @param editPurpose   A String explaining why this reservation was cancelled
     */
    public void cancelReservation(String editPurpose) {
        this.cancelled = true;
        this.editPurpose = editPurpose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return cancelled == that.cancelled
                && Objects.equals(id, that.id)
                && Objects.equals(madeBy, that.madeBy)
                && Objects.equals(roomId, that.roomId)
                && Objects.equals(start, that.start)
                && Objects.equals(end, that.end)
                && type == that.type
                && Objects.equals(userId, that.userId)
                && Objects.equals(groupId, that.groupId)
                && Objects.equals(purpose, that.purpose)
                && Objects.equals(editPurpose, that.editPurpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, madeBy, roomId, start, end, type, userId, groupId,
                purpose, editPurpose, cancelled);
    }
}
