package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import java.time.LocalDateTime;

public class ReservationBuilder {

    private Long roomId;
    //user who made the reservation
    private Long userId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long groupId;
    private String purpose;
    private ReservationType type;

    public ReservationBuilder(Long userId, Long roomId, LocalDateTime start, LocalDateTime end) {
        this.userId = userId;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
    }

    public void group(Long groupId) {
        this.groupId = groupId;
    }
    public void purpose(String message) {
        this.purpose = purpose;
    }
    public void type(ReservationType type) {
        this.type = type;
    }

    public Reservation build() {
        return new Reservation(roomId, userId, start, end, groupId, purpose, type);
    }
}