package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import java.time.LocalDateTime;

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

    public ReservationBuilder(Long madeBy, Long roomId, LocalDateTime start, LocalDateTime end) {
        this.madeBy = madeBy;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
    }

    public void type(ReservationType type) {
        this.reservationType = type;
    }
    public void user(Long groupId) { this.userId = userId; }
    public void group(Long groupId) {
        this.groupId = groupId;
    }
    public void purpose(String message) {
        this.purposeMessage = message;
    }

    public Reservation build() {
        return new Reservation(madeBy, roomId, start, end, reservationType, userId, groupId, purposeMessage);
    }
}