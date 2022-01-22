package nl.tudelft.sem.reservation.communication;

import  static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;


class UserControllerTest {

    private transient Reservation reservation;
    private transient List<Reservation> schedule;

    @Mock
    transient ReservationRepository reservationRepo;

    private transient UserController controller;
    private final transient String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new UserController(reservationRepo);

        // setup objects
        reservation = new Reservation(89L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");

        schedule = List.of(reservation);
    }

    @Test
    void checkUser() {
        when(reservationRepo.findById(any())).thenReturn(Optional.ofNullable(reservation));
        assertTrue(controller.checkUser(89L, 2L));
    }

    @Test
    void checkUserException() {
        when(reservationRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            controller.checkUser(89L, 2L);
        });
    }

    @Test
    void getSchedule() {
        when(reservationRepo.findByUserId(any())).thenReturn(schedule);
        assertEquals(schedule, controller.getSchedule(2L));
    }
}