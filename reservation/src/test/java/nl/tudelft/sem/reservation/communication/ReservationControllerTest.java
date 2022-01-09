package nl.tudelft.sem.reservation.communication;


import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    // define objects
    private transient Reservation reservation1;
    private transient Reservation reservation2;
    private transient Reservation reservation3;
    private transient Reservation reservation4;

    @Mock
    transient ReservationRepository reservationRepo;

    private transient ReservationController controller;
    private transient ReservationController spyController;
    private transient final String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ReservationController(reservationRepo);

        // setup objects
        reservation1 = new Reservation(45L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 45L, -1L, "Scrum meeting");
        reservation2 = new Reservation(53L, 5L, LocalDateTime.parse("2022-01-09T13:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T19:22:23.643606500"), ReservationType.SELF, 53L, -1L,
                "Project meeting");
        reservation3 = new Reservation(37L, 5L,
                LocalDateTime.parse("2022-01-09T10:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T13:10:23.643606500"),
                ReservationType.GROUP, -1L, 24L, "Research group meeting");
        reservation4 = new Reservation(1L, 2L,
                LocalDateTime.parse("2022-01-09T12:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T13:52:23.643606500"),
                ReservationType.ADMIN, 89L, -1L, "Have a lunch room");

        // mock ReservationRepository
        lenient().when(reservationRepo.
                findAllByRoomIdInAndCancelledIsFalseAndStartBeforeAndEndAfter(
                        Arrays.asList(2L, 3L, 5L, 7L),
                        LocalDateTime.parse("2022-01-09T09:30:00.643606500"),
                        LocalDateTime.parse("2022-01-09T14:00:00.643606500")))
                .thenReturn(Arrays.asList(reservation2, reservation3, reservation4));

        // mock static communication methods
        spyController = Mockito.spy(controller);
    }

    /**
     * Test the checking of a timeslot.
     */

    @Test
    void checkTimeSlotTest() {
        // variables for the method call and verify
        List<Long> rooms = Arrays.asList(2L, 3L, 5L, 7L);
        String start = "2022-01-09T09:30:00.643606500";
        String end = "2022-01-09T14:00:00.643606500";

        List<Long> roomResultList = Arrays.asList(3L, 7L);
        List<Long> roomTestList = spyController.checkTimeslot(rooms,
                start, end);

        verify(reservationRepo, times(1))
                .findAllByRoomIdInAndCancelledIsFalseAndStartBeforeAndEndAfter(rooms,
                        LocalDateTime.parse(start), LocalDateTime.parse(end));
        assertEquals(roomResultList, roomTestList);
    }
}
