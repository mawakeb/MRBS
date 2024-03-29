package nl.tudelft.sem.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeesCannotReserveMoreThanTwoWeeksInAdvanceTest {

    private transient Reservation reservation;
    private transient EmployeesCannotReserveMoreThanTwoWeeksInAdvance validator;
    private final transient String token = "token";


    @BeforeEach
    void setUp() {
        validator = new EmployeesCannotReserveMoreThanTwoWeeksInAdvance();
    }

    @Test
    void noMoreThanTwoWeeks() throws InvalidReservationException {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Amsterdam"));
        reservation = new Reservation(89L, 9L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                now,
                ReservationType.SELF, 89L, -1L, "Scrum meeting");
        assertTrue(validator.handle(reservation, token));
    }

    @Test
    void moreThanTwoWeeks() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Amsterdam"));
        reservation = new Reservation(89L, 9L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                now.plusWeeks(3),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");

        Exception e = assertThrows(InvalidReservationException.class,
                () -> validator.handle(reservation, token));

        String expectedMessage = "Reservation exceeds two-week limit.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}