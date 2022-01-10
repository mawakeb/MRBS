package nl.tudelft.sem.reservation.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import nl.tudelft.sem.reservation.builder.Builder;
import nl.tudelft.sem.reservation.builder.Director;
import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ReservationTest {

    private static LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5);
    private static LocalDateTime end = LocalDateTime.of(5, 4, 3, 2, 1);
    private static Reservation reservation;

    @BeforeEach
    void setUp() {

        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, "Test purpose");
        reservation = testBuilder.build();
    }

    @Test
    void getMadeByTest() {
        assertEquals(123L, reservation.getMadeBy());
    }

    @Test
    void getRoomIdTest() {
        assertEquals(321L, reservation.getRoomId());
    }

    @Test
    void getTimesTest() {
        assertEquals(start, reservation.getStart());
        assertEquals(end, reservation.getEnd());
    }

    @Test
    void getUserAndGroupIdTest() {
        assertEquals(234L, reservation.getUserId());
        assertEquals(345L, reservation.getGroupId());
    }

    @Test
    void getPurposeTest() {
        assertEquals("Test purpose", reservation.getPurpose());
    }

    @Test
    void getTypeTest() {
        assertEquals(ReservationType.SINGLE, reservation.getType());
    }

    @Test
    void isCancelledTest() {
        assertFalse(reservation.isCancelled());
    }

    @Test
    public void changeLocationTest() {
        reservation.changeLocation(789L, "Location edit purpose");

        assertEquals(789L, reservation.getRoomId());
        assertEquals("Location edit purpose", reservation.getEditPurpose());
    }

    @Test
    public void changeTimeTest() {
        LocalDateTime newStart = LocalDateTime.of(2, 3, 4, 5, 6);
        LocalDateTime newEnd = LocalDateTime.of(6, 5, 4, 3, 2);
        reservation.changeTime(newStart, newEnd, "Time edit purpose");

        assertEquals(newStart, reservation.getStart());
        assertEquals(newEnd, reservation.getEnd());
        assertEquals("Time edit purpose", reservation.getEditPurpose());
    }

    @Test
    public void cancelReservationTest() {
        reservation.cancelReservation("Cancel purpose");

        assertTrue(reservation.isCancelled());
        assertEquals("Cancel purpose", reservation.getEditPurpose());

    }
}