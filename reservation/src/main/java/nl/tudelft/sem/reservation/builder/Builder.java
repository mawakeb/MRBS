package nl.tudelft.sem.reservation.builder;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import java.time.LocalDateTime;

public interface Builder {
    public void type(ReservationType type);
    public void user(Long groupId);
    public void group(Long groupId);
    public void purpose(String message);

    public Reservation build();
}