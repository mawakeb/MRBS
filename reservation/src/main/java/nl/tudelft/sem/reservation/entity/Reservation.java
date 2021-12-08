package nl.tudelft.sem.reservation.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @Column(name = "roomId")
    private int roomId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "editPurpose")
    private String editPurpose;

    @Column(name = "cancelled")
    private boolean cancelled;


    /**
     * Constructor for the Reservation class.
     *
     * @param id      unique reservation ID.
     * @param roomId  location of the reservation.
     * @param userId  netID of the user who made the reservation.
     * @param start   Date and time of the start of the reservation
     * @param end     Date and time of the end of the reservation
     * @param purpose purpose of the reservation
     */
    public Reservation(int id, int roomId, long userId, LocalDateTime start,
                       LocalDateTime end, String purpose) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.cancelled = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
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

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void changeLocation(int newRoomId, String editPurpose) {
        this.roomId = newRoomId;
        this.editPurpose = editPurpose;
    }

    public void changeTime(LocalDateTime newStart, LocalDateTime newEnd, String editPurpose) {
        this.start = newStart;
        this.end = newEnd;
        this.editPurpose = editPurpose;
    }

    public void cancelReservation(String editPurpose) {
        this.cancelled = true;
        this.editPurpose = editPurpose;
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
