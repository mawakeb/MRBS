package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.communication.UserCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeesMakeEditCancelReservationForThemselvesTest {

    private transient Reservation reservation;
    private transient EmployeesMakeEditCancelReservationForThemselves validator;
    private final transient String token = "token";


    @BeforeEach
    void setUp() {
        validator = Mockito.spy(new EmployeesMakeEditCancelReservationForThemselves());
        reservation = Mockito.mock(Reservation.class);
    }

    @Test
    void NotEmployee() throws InvalidReservationException {
        Mockito.doReturn("ADMIN").when(validator).getUserType(token);
        assertTrue(validator.handle(reservation, token));

        Mockito.doReturn("SECRETARY").when(validator).getUserType(token);
        assertTrue(validator.handle(reservation, token));
    }

    @Test
    void ForThemself() throws InvalidReservationException {
        Mockito.doReturn("EMPLOYEE").when(validator).getUserType(token);
        Mockito.when(reservation.getMadeBy()).thenReturn(1L);
        Mockito.when(reservation.getUserId()).thenReturn(1L);
        assertTrue(validator.handle(reservation, token));
    }

    @Test
    void NotForThemself() {
        Mockito.doReturn("EMPLOYEE").when(validator).getUserType(token);
        Mockito.when(reservation.getMadeBy()).thenReturn(1L);
        Mockito.when(reservation.getUserId()).thenReturn(2L);

        Exception e = assertThrows(InvalidReservationException.class,
                () -> validator.handle(reservation, token));

        String expectedMessage = "Employees cannot manage reservations for someone else.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}