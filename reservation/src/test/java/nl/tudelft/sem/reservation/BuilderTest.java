package nl.tudelft.sem.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import nl.tudelft.sem.reservation.builder.Director;
import nl.tudelft.sem.reservation.builder.Builder;
import nl.tudelft.sem.reservation.builder.ReservationBuilder;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class BuilderTest {

    static Long madeBy = 123L;
    static Long roomId = 321L;
    static LocalDateTime start = LocalDateTime.of(1,2,3,4,5);
    static LocalDateTime end = LocalDateTime.of(5,4,3,2,1);
    static Builder testBuilder;
    static Director testDirector;

    @BeforeEach
    public void setValues()
    {
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

        testDirector.buildGroupReservation(345L, "Test purpose");
        Reservation testReservation = testBuilder.build();

        assertEquals(madeBy, testReservation.getMadeBy());
        assertEquals(roomId, testReservation.getRoomId());
        assertEquals(start, testReservation.getStart());
        assertEquals(end, testReservation.getEnd());
        assertEquals(345L, testReservation.getGroupId());
        assertEquals("Test purpose", testReservation.getPurpose());
        assertEquals(ReservationType.GROUP, testReservation.getType());
    }
}
