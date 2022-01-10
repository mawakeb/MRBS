package nl.tudelft.sem.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckIfRoomIsNotReservedAlreadyTest {

    private transient CheckIfRoomIsNotReservedAlready spyValidator;
    private final transient String token = "token";
    private transient Reservation reservation;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reservation = new Reservation(89L, 9L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");

        spyValidator = Mockito.spy(new CheckIfRoomIsNotReservedAlready());

        // mock spy calls
        lenient().doReturn(new ArrayList<>()).when(spyValidator)
                .findAllForSpecificRoomWithinGivenTimeRange(anyLong(), any(), any());
    }

    @Test
    void testHandleNoOverlap() throws InvalidReservationException {
        doReturn(new ArrayList<Reservation>()).when(spyValidator)
                .findAllForSpecificRoomWithinGivenTimeRange(anyLong(), any(), any());
        assertTrue(spyValidator.handle(reservation, token));
    }

    @Test
    void testHandleOverlappingReservations() {
        doReturn(new ArrayList<>(List.of(reservation))).when(spyValidator)
                .findAllForSpecificRoomWithinGivenTimeRange(anyLong(), any(), any());

        Exception e = assertThrows(InvalidReservationException.class,
                () -> spyValidator.handle(reservation, token));

        String expectedMessage = "There is another reservation overlapping "
                + "with your desired time range.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}