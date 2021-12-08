package nl.tudelft.sem.room.entity;

import javax.persistence.*;

@Entity
@Table(name = "roomNotice")
public class RoomNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "roomId")
    private Long roomId;

    @Column(name = "reservationId")
    private Long reservationId;

    @Column(name = "message")
    private String message;

    public RoomNotice(Long roomId, Long reservationId, String message) {
        this.roomId = roomId;
        this.reservationId = reservationId;
        this.message = message;
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

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}