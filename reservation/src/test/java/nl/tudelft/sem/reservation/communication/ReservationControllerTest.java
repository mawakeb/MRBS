package nl.tudelft.sem.reservation.communication;


import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.entity.ReservationType;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private transient final String adminToken = "adminToken";
    private transient final String token = "token";

    @BeforeEach
    void setUp() throws InvalidReservationException {
        MockitoAnnotations.initMocks(this);
        controller = new ReservationController(reservationRepo);

        // setup objects
        reservation1 = new Reservation(89L, 3L,
                LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T17:22:23.643606500"),
                ReservationType.SELF, 89L, -1L, "Scrum meeting");
        reservation2 = new Reservation(53L, 5L,
                LocalDateTime.parse("2022-01-09T13:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T19:22:23.643606500"),
                ReservationType.SELF, 53L, -1L, "Project meeting");
        reservation3 = new Reservation(37L, 5L,
                LocalDateTime.parse("2022-01-09T10:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T13:10:23.643606500"),
                ReservationType.GROUP, -1L, 24L, "Research group meeting");
        reservation4 = new Reservation(1L, 2L,
                LocalDateTime.parse("2022-01-09T12:22:23.643606500"),
                LocalDateTime.parse("2022-01-09T13:52:23.643606500"),
                ReservationType.ADMIN, 37L, -1L, "Have a lunch room");

        // mock ReservationRepository
        lenient().when(reservationRepo.findById(0L)).thenReturn(Optional.ofNullable(reservation1));
        lenient().when(reservationRepo.findById(1L)).thenReturn(Optional.ofNullable(reservation2));
        lenient().when(reservationRepo.findById(2L)).thenReturn(Optional.ofNullable(reservation3));
        lenient().when(reservationRepo.findById(3L)).thenReturn(Optional.ofNullable(reservation4));
        lenient().when(reservationRepo.findById(5L)).thenReturn(Optional.empty());

        lenient().when(reservationRepo.
                findAllByRoomIdInAndCancelledIsFalseAndStartBeforeAndEndAfter(
                        Arrays.asList(2L, 3L, 5L, 7L),
                        LocalDateTime.parse("2022-01-09T09:30:00.643606500"),
                        LocalDateTime.parse("2022-01-09T14:00:00.643606500")))
                .thenReturn(Arrays.asList(reservation2, reservation3, reservation4));

        // mock static communication methods
        spyController = Mockito.spy(controller);
        lenient().doReturn("EMPLOYEE").when(spyController).getUserType(token);
        lenient().doReturn("ADMIN").when(spyController).getUserType(adminToken);
        lenient().doReturn(37L).when(spyController).getUser(token);
        lenient().doReturn(true).when(spyController).handle(any(), any(), any());
    }

    /**
     * Test the editing and cancelling of an invalid reservation.
     */
    @Test
    void editAndCancelInvalidReservation() {
        String editResult = spyController.editReservation(5L, 7L,
                null, null, "Entered wrong room", token);
        String cancelResult = spyController.cancelReservation(5L, "Got covid", token);

        verify(reservationRepo, times(2)).findById(5L);
        assertEquals("Reservation was not found", editResult);
        assertEquals("Reservation was not found", cancelResult);
    }

    /**
     * Test the editing and cancelling when they don't have permission.
     */
    @Test
    void editAndCancelReservationNoPermission() {
        String editResult = spyController.editReservation(1L, 7L,
                null, null, "Entered wrong room", token);
        String cancelResult = spyController.cancelReservation(1L, "Got covid", token);

        verify(reservationRepo, times(2)).findById(1L);
        assertEquals("You do not have access to edit this reservation", editResult);
        assertEquals("You do not have access to cancel this reservation", cancelResult);
    }

    /**
     * Test the editing and cancelling when the user is admin.
     * And this is the only reason the user should have permissions.
     */
    @Test
    void editAndCancelReservationAsAdmin() throws InvalidReservationException {
        String editResult = spyController.editReservation(1L, 7L,
                null, null, "Entered wrong room", adminToken);
        assertEquals("Entered wrong room", reservation2.getEditPurpose());
        String cancelResult = spyController.cancelReservation(1L, "Got covid", adminToken);

        verify(reservationRepo, times(2)).findById(1L);
        verify(spyController, times(1)).handle(any(), any(), any());
        verify(reservationRepo, times(2)).save(any(Reservation.class));

        assertEquals("Reservation was edited successfully", editResult);
        assertEquals("Reservation was cancelled successfully", cancelResult);
        assertEquals(7L, reservation2.getRoomId());
        assertTrue(reservation2.isCancelled());
        assertEquals("Got covid", reservation2.getEditPurpose());
    }

    /**
     * Test the editing and cancelling when the reservation was made by the user.
     * And this is the only reason the user should have permissions.
     */
    @Test
    void editAndCancelReservationMadeByUser() throws InvalidReservationException {
        String editResult = spyController.editReservation(2L, 7L,
                null, null, "Entered wrong room", token);
        assertEquals("Entered wrong room", reservation3.getEditPurpose());
        String cancelResult = spyController.cancelReservation(2L, "Got covid", token);

        verify(reservationRepo, times(2)).findById(2L);
        verify(spyController, times(1)).handle(any(), any(), any());
        verify(reservationRepo, times(2)).save(any(Reservation.class));

        assertEquals("Reservation was edited successfully", editResult);
        assertEquals("Reservation was cancelled successfully", cancelResult);
        assertEquals(7L, reservation3.getRoomId());
        assertTrue(reservation3.isCancelled());
        assertEquals("Got covid", reservation3.getEditPurpose());
    }

    /**
     * Test the editing and cancelling when the reservation was made for the user.
     * And this is the only reason the user should have permissions.
     */
    @Test
    void editAndCancelReservationForTheUser() throws InvalidReservationException {
        String editResult = spyController.editReservation(3L, 7L,
                null, null, "Entered wrong room", token);
        assertEquals("Entered wrong room", reservation4.getEditPurpose());
        String cancelResult = spyController.cancelReservation(3L, "Got covid", token);

        verify(reservationRepo, times(2)).findById(3L);
        verify(spyController, times(1)).handle(any(), any(), any());
        verify(reservationRepo, times(2)).save(any(Reservation.class));

        assertEquals("Reservation was edited successfully", editResult);
        assertEquals("Reservation was cancelled successfully", cancelResult);
        assertEquals(7L, reservation4.getRoomId());
        assertTrue(reservation4.isCancelled());
        assertEquals("Got covid", reservation4.getEditPurpose());
    }

    /**
     * Test the editing of a reservation when there is nothing to edit.
     */
    @Test
    void editReservationWithoutEdits() {
        String editResult = spyController.editReservation(0L, -1L,
                null, null, "Yup", token);

        verify(reservationRepo, times(1)).findById(0L);
        assertEquals("There is nothing to edit for the reservation", editResult);
        assertEquals(3L, reservation1.getRoomId());
        assertEquals(LocalDateTime.parse("2022-01-09T14:22:23.643606500"),
                reservation1.getStart());
        assertEquals(LocalDateTime.parse("2022-01-09T17:22:23.643606500"), reservation1.getEnd());
        //assertEquals("Yup", reservation1.getEditPurpose());
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
        List<Long> roomTestList = spyController.checkTimeslot(rooms, start, end);

        verify(reservationRepo, times(1))
                .findAllByRoomIdInAndCancelledIsFalseAndStartBeforeAndEndAfter(rooms,
                        LocalDateTime.parse(start), LocalDateTime.parse(end));
        assertEquals(roomResultList, roomTestList);
    }
}
