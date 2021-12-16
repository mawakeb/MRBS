package nl.tudelft.sem.reservation.entity;

import nl.tudelft.sem.reservation.entity.ReservationType;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.Objects;

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
     * @param id      Unique reservation ID.
     * @param madeBy  ID of the user who made the reservation.
     * @param roomId  Location of the reservation.
     * @param start   Date and time of the start of the reservation.
     * @param end     Date and time of the end of the reservation.
     * @param type    Whether this reservation was made for the user themselves, another user or a research group.
     * @param userId  The ID of the person the reservation was made for.
     * @param groupId The ID of the research group the reservation was made for.
     * @param purpose purpose of the reservation
     */
    public Reservation(Long madeBy, Long roomId, LocalDateTime start, LocalDateTime end, ReservationType type, Long userId, Long groupId, String purpose) {
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

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    private void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getMadeBy() {
        return madeBy;
    }

    private void setMadeBy(Long madeBy) {
        this.madeBy = madeBy;
    }

    public LocalDateTime getStart() {
        return start;
    }

    private void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    private void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getPurpose() {
        return purpose;
    }

    private void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getEditPurpose() {
        return editPurpose;
    }

    public void setEditPurpose(String editPurpose) {
        this.editPurpose = editPurpose;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void changeLocation(Long newRoomId, String editPurpose) {
        this.setRoomId(newRoomId);
        this.setEditPurpose(editPurpose);
    }

    public void changeTime(LocalDateTime newStart, LocalDateTime newEnd, String editPurpose) {
        this.setStart(newStart);
        this.setEnd(newEnd);
        this.setEditPurpose(editPurpose);
    }

    public void cancelReservation(String editPurpose) {
        this.setCancelled(true);
        this.setEditPurpose(setEditPurpose());
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
