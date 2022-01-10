package nl.tudelft.sem.reservation.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class BuilderTest {

    private static Long madeBy = 123L;
    private static Long roomId = 321L;
    private static LocalDateTime start = LocalDateTime.of(1, 2, 3, 4, 5);
    private static LocalDateTime end = LocalDateTime.of(5, 4, 3, 2, 1);
    private static String testPurpose = "Test";
    private static Builder testBuilder;
    private static Director testDirector;

    @BeforeEach
    public void setValues() {
        testBuilder = new ReservationBuilder(madeBy, roomId, start, end);
        testDirector = new Director(testBuilder);
    }

    @Test
    public void selfReservationTest() {

        testDirector.buildSelfReservation();
        Reservation testReservation = testBuilder.build();

        assertEquals(madeBy, testReservation.getMadeBy());
        assertEquals(roomId, testReservation.getRoomId());
        assertEquals(start, testReservation.getStart());
        assertEquals(end, testReservation.getEnd());
        assertEquals(madeBy, testReservation.getUserId());
        assertEquals(ReservationType.SELF, testReservation.getType());
    }

    @Test
    public void singleReservationTest() {

        testDirector.buildSingleReservation(234L, 345L, testPurpose);
        Reservation testReservation = testBuilder.build();

        assertEquals(madeBy, testReservation.getMadeBy());
        assertEquals(roomId, testReservation.getRoomId());
        assertEquals(start, testReservation.getStart());
        assertEquals(end, testReservation.getEnd());
        assertEquals(234L, testReservation.getUserId());
        assertEquals(345L, testReservation.getGroupId());
        assertEquals("Test", testReservation.getPurpose());
        assertEquals(ReservationType.SINGLE, testReservation.getType());
    }

    @Test
    public void adminReservationTest() {

        testDirector.buildAdminReservation(234L);
        Reservation testReservation = testBuilder.build();

        assertEquals(madeBy, testReservation.getMadeBy());
        assertEquals(roomId, testReservation.getRoomId());
        assertEquals(start, testReservation.getStart());
        assertEquals(end, testReservation.getEnd());
        assertEquals(234L, testReservation.getUserId());
        assertEquals(ReservationType.ADMIN, testReservation.getType());
    }

    @Test
    public void groupReservationTest() {

        testDirector.buildGroupReservation(345L, testPurpose);
        Reservation testReservation = testBuilder.build();

        assertEquals(madeBy, testReservation.getMadeBy());
        assertEquals(roomId, testReservation.getRoomId());
        assertEquals(start, testReservation.getStart());
        assertEquals(end, testReservation.getEnd());
        assertEquals(345L, testReservation.getGroupId());
        assertEquals("Test", testReservation.getPurpose());
        assertEquals(ReservationType.GROUP, testReservation.getType());
    }
}
