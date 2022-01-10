package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

public class SecretariesCanOnlyReserveEditForTheirResearchMembersTest {
    // define objects
    Reservation reservation1;
    Reservation reservation2;

    private transient SecretariesCanOnlyReserveEditForTheirResearchMembers spyValidator;
    private final transient String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        spyValidator = Mockito.spy(new SecretariesCanOnlyReserveEditForTheirResearchMembers());

        // setup objects
        reservation1 = new Reservation(13L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SINGLE, 89L, 5L, "Scrum meeting");
        reservation2 = new Reservation(13L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.GROUP, 89L, 5L, "Scrum meeting");
    }

    /**
     * Test the validator when it is a single or admin reservation.
     */
    @Test
    void selfOrAdminReservationTest() throws InvalidReservationException {
        Reservation reservation3 = new Reservation(89L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");
        Reservation reservation4 = new Reservation(1L, 2L,
                LocalDateTime.parse("2022-01-09T12:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T13:52:23.643606500"),
                ReservationType.ADMIN, 37L, -1L, "Have a lunch room");

        assertTrue(spyValidator.handle(reservation3, token));
        assertTrue(spyValidator.handle(reservation4, token));
    }

    // single type reservation tests
    /**
     * Test the validator when it is for a reservation made by a secretary for its group member.
     */
    @Test
    void madeBySecretaryForOwnGroupMemberTest() throws InvalidReservationException {
        doReturn(true).when(spyValidator).isSecretaryOfGroup(13L, 5L, token);
        doReturn(true).when(spyValidator).isInGroup(89L, 5L, token);

        assertTrue(spyValidator.handle(reservation1, token));
    }

    /**
     * Test the validator when it is for a reservation made by a secretary not for its group member.
     */
    @Test
    void madeBySecretaryNotForGroupMemberTest() {
        doReturn(true).when(spyValidator).isSecretaryOfGroup(13L, 5L, token);
        doReturn(false).when(spyValidator).isInGroup(89L, 5L, token);

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation1, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    /**
     * Test the validator when it is for a reservation made by a secretary not in the group for a group member.
     */
    @Test
    void madeBySecretaryNotInGroupForGroupMemberTest() {
        doReturn(false).when(spyValidator).isSecretaryOfGroup(13L, 5L, token);
        doReturn(true).when(spyValidator).isInGroup(89L, 5L, token);

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation1, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }

    // group type reservation tests
    /**
     * Test the validator when it is for a reservation made by a secretary in the given group.
     */
    @Test
    void madeBySecretaryInGivenGroupMemberTest() throws InvalidReservationException {
        doReturn(true).when(spyValidator).isSecretaryOfGroup(13L, 5L, token);

        assertTrue(spyValidator.handle(reservation2, token));
    }

    /**
     * Test the validator when it is for a reservation made by a secretary not in the given group.
     */
    @Test
    void madeBySecretaryNotInGivenGroupMemberTest() {
        doReturn(false).when(spyValidator).isSecretaryOfGroup(13L, 5L, token);

        assertThrows(InvalidReservationException.class, () ->
                        spyValidator.handle(reservation2, token),
                "Expected handle(reservation, token) to throw, but it didn't");
    }
}
