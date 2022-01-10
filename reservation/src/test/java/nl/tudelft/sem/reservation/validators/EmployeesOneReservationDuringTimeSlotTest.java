package nl.tudelft.sem.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EmployeesOneReservationDuringTimeSlotTest {
    // define objects
    private transient Reservation reservation1;
    private transient Reservation reservation2;
    private transient Reservation reservation3;

    private transient EmployeesOneReservationDuringTimeSlot spyValidator;
    private final transient String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        spyValidator = Mockito.spy(new EmployeesOneReservationDuringTimeSlot());

        // setup objects
        reservation1 = new Reservation(13L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, 5L, "Scrum meeting");
        reservation2 = new Reservation(13L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.GROUP, 89L, 5L, "Scrum meeting");
        reservation3 = new Reservation(29L, 3L,
                LocalDateTime.parse("2022-01-09T15:00:00.643606500"),
                LocalDateTime.parse("2022-01-09T17:00:00.643606500"),
                ReservationType.GROUP, 61L, 7L, "Scrum meeting");
    }

    // not group reservation tests
    /**
     * Test the handle of the validator when the has another meeting.
     */
    @Test
    void userIsOverbookedTest() {
        doReturn(List.of(reservation2)).when(spyValidator)
                .findAllOverlappingWithGivenReservationByUserId(anyLong(), any(), any());

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation1, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    /**
     * Test the handle of the validator when the user has no other meeting.
     */
    @Test
    void userIsFreeTest() throws InvalidReservationException {
        doReturn(List.of()).when(spyValidator)
                .findAllOverlappingWithGivenReservationByUserId(anyLong(), any(), any());

        assertTrue(spyValidator.handle(reservation1, token));
    }

    // group reservation tests
    /**
     * Test the handle of the validator when there is another not group
     * reservation at the same time for a group member.
     */
    @Test
    void groupMemberIsOverbookedNotDoubleGroupTest() {
        doReturn(List.of(reservation1)).when(spyValidator).findAllOverlapping(any(), any());
        doReturn(true).when(spyValidator).isInGroup(89L, 5L, token);

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation2, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    /**
     * Test the handle of the validator when there is another group
     * reservation at the same time with at least one overlapping member.
     */
    @Test
    void groupMemberIsOverbookedDoubleGroupTest() {
        doReturn(List.of(reservation3)).when(spyValidator).findAllOverlapping(any(), any());
        doReturn(true).when(spyValidator).overlap(5L, 7L, token);

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation2, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    /**
     * Test the handle of the validator when there isn't another group
     * reservation at the same time with at least one overlapping member.
     */
    @Test
    void noGroupMemberIsOverbookedDoubleGroupTest() throws InvalidReservationException {
        doReturn(List.of(reservation3)).when(spyValidator).findAllOverlapping(any(), any());
        doReturn(false).when(spyValidator).overlap(5L, 7L, token);

        assertTrue(spyValidator.handle(reservation2, token));
    }
}
