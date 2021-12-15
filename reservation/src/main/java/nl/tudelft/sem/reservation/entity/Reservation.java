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

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "userId")
    private Long userId;

    private Long groupId;

    private ReservationType type;

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
    private Reservation(ReservationBuilder builder) {
        this.type = builder.type;
        this.roomId = builder.roomId;
        this.userId = builder.userId;
        this.start = builder.start;
        this.end = builder.end;
        this.purpose = builder.purpose;
        this.cancelled = false;
    }

    public class ReservationBuilder {

        private ReservationType type;
        private Long roomId;
        private Long userId;
        private Long groupId;
        private LocalDateTime start;
        private LocalDateTime end;
        private String purpose;

        public ReservationBuilder(Long roomId, LocalDateTime start, LocalDateTime end) {
            this.roomId = roomId;
            this.start = start;
            this.end = end;
        }

        public ReservationBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public Reservation buildSingleReservation(Long userId) {
            this.type = ReservationType.SINGLE;
            this.userId = userId;
            return new Reservation(this);
        }
        public Reservation buildGroupReservation(Long groupId) {
            this.type = ReservationType.GROUP;
            this.groupId = groupId;
            return new Reservation(this);
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
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

    public void changeLocation(Long newRoomId, String editPurpose) {
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
