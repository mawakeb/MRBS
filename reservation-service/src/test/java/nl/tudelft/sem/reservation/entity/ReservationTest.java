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
    private static final String purpose = "Test purpose";

    @BeforeEach
    void setUp() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
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
        assertEquals(purpose, reservation.getPurpose());
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

    // equals method tests
    @Test
    public void equalTest() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertTrue(reservation.equals(other));
    }

    @Test
    public void differentCancelled() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        other.cancelReservation(null);

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentMadeBy() {
        Builder testBuilder = new ReservationBuilder(456L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentRoomId() {
        Builder testBuilder = new ReservationBuilder(123L, 654L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentStart() {
        LocalDateTime otherStart = LocalDateTime.of(1, 2, 3, 4, 6);
        Builder testBuilder = new ReservationBuilder(123L, 321L, otherStart, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentEnd() {
        LocalDateTime otherEnd = LocalDateTime.of(5, 4, 3, 2, 0);
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, otherEnd);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentType() {
        Builder testBuilder1 = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector1 = new Director(testBuilder1);
        testDirector1.buildSelfReservation();
        Reservation one = testBuilder1.build();

        Builder testBuilder2 = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector2 = new Director(testBuilder2);
        testDirector2.buildAdminReservation(123L);
        Reservation other = testBuilder2.build();

        assertFalse(one.equals(other));
    }

    @Test
    public void differentUserId() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(567L, 345L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentGroupId() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 678L, purpose);
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentPurpose() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, "Totally not testing purpose");
        Reservation other = testBuilder.build();

        assertFalse(reservation.equals(other));
    }

    @Test
    public void differentEditPurpose() {
        Builder testBuilder = new ReservationBuilder(123L, 321L, start, end);
        Director testDirector = new Director(testBuilder);
        testDirector.buildSingleReservation(234L, 345L, purpose);
        Reservation other = testBuilder.build();

        other.changeLocation(321L, "Testing purposes");

        assertFalse(reservation.equals(other));
    }
}