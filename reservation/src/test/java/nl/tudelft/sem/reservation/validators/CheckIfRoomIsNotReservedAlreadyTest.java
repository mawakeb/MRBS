package nl.tudelft.sem.reservation.validators;

import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class CheckIfRoomIsNotReservedAlreadyTest {
    @Mock
    private ReservationRepository reservationRepo;
    @InjectMocks
    @Resource
    private transient CheckIfRoomIsNotReservedAlready validator;
    private final transient String token = "token";
    private Reservation reservation;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationRepo = Mockito.mock(ReservationRepository.class);
        reservation = new Reservation(89L, 9L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");

        validator = new CheckIfRoomIsNotReservedAlready();
    }

    @Test
    void testHandleNoOverlap() throws InvalidReservationException {
        Mockito.when(reservationRepo.findAllForSpecificRoomWithinGivenTimeRange(any(),any(),any())).thenReturn(new ArrayList<Reservation>());
        assertTrue(validator.handle(reservation, token));
    }

    @Test
    void testHandleOverlappingReservations() throws InvalidReservationException {
        List<Reservation> list = new ArrayList<>();
        list.add(reservation);
        doReturn(list).when(reservationRepo).findAllForSpecificRoomWithinGivenTimeRange(any(),any(),any());

        Exception e = assertThrows(InvalidReservationException.class,
                () -> validator.handle(reservation, token));
    }
}