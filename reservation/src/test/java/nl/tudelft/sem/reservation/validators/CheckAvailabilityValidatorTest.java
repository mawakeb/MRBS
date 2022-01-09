package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CheckAvailabilityValidatorTest {

    private transient CheckAvailabilityValidator spyValidator;
    private final transient String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        spyValidator = Mockito.spy(new CheckAvailabilityValidator());
    }

    /**
     * Test the handle of the validator when the room isn't available.
     */
    @Test
    void roomNotAvailableTest() {
        // mock of static communication method
        doReturn(false).when(spyValidator).getRoomAvailability(any(),
                any(), any(), any());

        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        Reservation reservation = new Reservation(89L, 9L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");
        assertThrows(InvalidReservationException.class, () ->
                spyValidator.handle(reservation, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    /**
     * Test the handle of the validator when the room is available.
     */
    @Test
    void roomAvailableTest() throws InvalidReservationException {
        // mock of static communication method
        doReturn(true).when(spyValidator).getRoomAvailability(any(),
                any(), any(), any());

        Reservation reservation = new Reservation(89L, 7L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");
        assertTrue(spyValidator.handle(reservation, token));
    }
}
