package nl.tudelft.sem.reservation.entity;

import java.time.LocalDateTime;
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

    private Long madeBy;

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    private ReservationType type;

    private Long userId;

    private Long groupId;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "editPurpose")
    private String editPurpose;

    @Column(name = "cancelled")
    private boolean cancelled;

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
     * Set the roomId.
     *
     * @param roomId    A Long representing the new room this reservation should be in
     */
    private void setRoomId(Long roomId) {
        this.roomId = roomId;
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
     * Set madeBy.
     *
     * @param madeBy    A Long representing the user who made this reservation
     */
    private void setMadeBy(Long madeBy) {
        this.madeBy = madeBy;
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
     * Set the start time.
     *
     * @param start A LocalDateTime representing the time this reservation should start
     */
    private void setStart(LocalDateTime start) {
        this.start = start;
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
     * Set the end time.
     *
     * @param end   A LocalDateTime representing the time this reservation should end
     */
    private void setEnd(LocalDateTime end) {
        this.end = end;
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
     * Set the reservationPurpose.
     *
     * @param purpose a string containing the purpose of this reservation
     */
    private void setPurpose(String purpose) {
        this.purpose = purpose;
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
     * Set the edit purpose.
     *
     * @param editPurpose a string containing the purpose for editing this reservation
     */
    public void setEditPurpose(String editPurpose) {
        this.editPurpose = editPurpose;
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
     * Set the reservation type.
     *
     * @param type the new type to set the reservation to
     */
    private void setType(ReservationType type) {
        this.type = type;
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
     * Set the userId.
     *
     * @param userId the new userId
     */
    private void setUserId(long userId) {
        this.userId = userId;
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
     * Set the groupId.
     *
     * @param groupId the new groupId
     */
    private void setGroupId(long groupId) {
        this.groupId = groupId;
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
     * Set cancelled.
     *
     * @param cancelled the new cancelled
     */
    private void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    /**
     * Changes the location of the reservation.
     *
     * @param newRoomId     A Long representing the new room the reservation should be set in
     * @param editPurpose   A String explaining why this reservation was moved
     */
    public void changeLocation(Long newRoomId, String editPurpose) {
        this.setRoomId(newRoomId);
        this.setEditPurpose(editPurpose);
    }

    /**
     * Changes the date and/or time of the reservation.
     *
     * @param newStart      A LocalDateTime representing when the reservation should start
     * @param newEnd        A LocalDateTime representing when the reservation should end
     * @param editPurpose   A String explaining why this reservation was rescheduled
     */
    public void changeTime(LocalDateTime newStart, LocalDateTime newEnd, String editPurpose) {
        this.setStart(newStart);
        this.setEnd(newEnd);
        this.setEditPurpose(editPurpose);
    }

    /**
     * Cancels the reservation.
     *
     * @param editPurpose   A String explaining why this reservation was cancelled
     */
    public void cancelReservation(String editPurpose) {
        this.setCancelled(true);
        this.setEditPurpose(editPurpose);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
