package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.EquipmentRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {
    // define objects
    private transient Room room1;
    private transient Room room2;
    private transient Room room3;
    private transient Room room4;

    @Mock
    transient RoomRepository roomRepo;
    @Mock
    transient EquipmentRepository equipmentRepo;
    @Mock
    transient ReservationCommunication reservationCommunication;

    private transient RoomController controller;
    private transient RoomController spyController;
    private transient List<Room> roomList;
    private final transient String token = "token";
    private final transient String testingUsedEquipment = "STAGE";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RoomController(roomRepo, equipmentRepo);
        roomList = List.of(new Room(1L, "meeting room1", 10L, 20),
                new Room(2L, "meeting room2", 10L, 20),
                new Room(3L, "meeting room3", 10L, 20));



        // setup objects
        room1 = new Room(4L, "CONTROL", 3L, 150);
        room2 = new Room(5L, "SHIFT", 7L, 500);
        room3 = new Room(6L, "ENTER", 5L, 250);
        room4 = new Room(7L, "NULL", 3L, 1000);

        //mock RoomRepository
        lenient().when(roomRepo.findAllById(Arrays.asList(4L, 7L)))
                .thenReturn(Arrays.asList(room1, room4));
        lenient().when(roomRepo.findAllById(Arrays.asList(6L, 7L)))
                .thenReturn(Arrays.asList(room3, room4));
        lenient().when(roomRepo.findAll()).thenReturn(Arrays.asList(room1, room2, room3, room4));


        //mock EquipmentRepository
        lenient().when(equipmentRepo.findAllByEquipmentName("")).thenReturn(List.of());
        lenient().when(equipmentRepo.findAllByEquipmentName(testingUsedEquipment))
                .thenReturn(Arrays.asList(4L, 7L));

        //mock static communication methods
        spyController = Mockito.spy(controller);
        lenient().doReturn(true).when(spyController).checkAdmin(token);
        lenient().doReturn(Arrays.asList(6L, 7L)).when(spyController).getRoomsInTimeslot(
                any(), any(), any(), any());

    }

    @Test
    void checkAvailable() {
        when(roomRepo.findById(1L)).thenReturn(roomList.get(0));
        boolean actual = controller.checkAvailable(1L,
                LocalTime.of(12, 0),
                LocalTime.of(14, 0));
        assertTrue(actual);
    }

    @Test
    void checkNotAvailable() {
        roomList.get(0).setUnderMaintenance(true);
        when(roomRepo.findById(1L)).thenReturn(roomList.get(0));
        boolean actual = controller.checkAvailable(1L,
                LocalTime.of(15, 0),
                LocalTime.of(16, 0));
        assertFalse(actual);
    }


    @Test
    void getById() {
        when(roomRepo.findById(any())).thenReturn(Optional.ofNullable(roomList.get(0)));
        assertEquals(roomList.get(0), controller.getById(1L));
    }

    @Test
    void changeStatus() {
        when(roomRepo.findById(1L)).thenReturn((roomList.get(0)));
        String success = spyController.changeStatus(token, 1L, true);
        assertEquals("Status changed successfully", success);
        assertTrue(roomList.get(0).isUnderMaintenance());
    }

    @Test
    void createRoom() {
        when(roomRepo.findById(5L)).thenReturn(null);
        String success = spyController.createRoom(token, 5L, "room3", 11L, 50);
        assertEquals("Room has been saved successfully", success);
        verify(roomRepo, times(1)).save(any(Room.class));
    }


    @Test
    void createEquipment() {
        String success = spyController.createEquipment(token, 1L, "chair");
        assertEquals("Equipment has been saved successfully", success);
        verify(equipmentRepo, times(1)).save(any(EquipmentInRoom.class));
    }


    // I used MC/DC to determine my test cases
    /**
     * Test the querying of rooms without parameters.
     */
    @Test
    public void queryRoomsNoParameters() {
        List<Room> roomResultList = Arrays.asList(room1, room2, room3, room4);
        List<Room> roomTestList = spyController.queryRooms(-1, -1,
                "", "", "", token);

        assertEquals(roomResultList, roomTestList);
        verify(roomRepo, times(1)).findAll();
        verify(equipmentRepo, never()).findAllByEquipmentName("");
        verifyNoInteractions(reservationCommunication);
    }

    /**
     * Test the querying of rooms with only capacity.
     */
    @Test
    void queryRoomsOnlyCapacity() {
        List<Room> roomResultList = Arrays.asList(room2, room4);
        List<Room> roomTestList = spyController.queryRooms(450, -1,
                "", "", "", token);

        assertEquals(roomResultList, roomTestList);
        verify(roomRepo, times(1)).findAll();
        verify(equipmentRepo, never()).findAllByEquipmentName("");
        verifyNoInteractions(reservationCommunication);
    }

    /**
     * Test the querying of rooms with only building.
     */
    @Test
    void queryRoomsOnlyBuilding() {
        List<Room> roomResultList = List.of(room2);
        List<Room> roomTestList = spyController.queryRooms(-1, 7,
                "", "", "", token);

        assertEquals(roomResultList, roomTestList);
        verify(roomRepo, times(1)).findAll();
        verify(equipmentRepo, never()).findAllByEquipmentName("");
        verifyNoInteractions(reservationCommunication);

    }

    /**
     * Test the querying of rooms with only equipment.
     */
    @Test
    void queryRoomsOnlyEquipment() {
        List<Room> roomResultList = List.of(room1, room4);
        List<Room> roomTestList = spyController.queryRooms(-1, -1,
                testingUsedEquipment, "", "", token);

        assertEquals(roomResultList, roomTestList);
        verify(roomRepo, never()).findAll();
        verify(roomRepo, times(1)).findAllById(any());
        verify(equipmentRepo, times(1)).findAllByEquipmentName(testingUsedEquipment);
        verifyNoInteractions(reservationCommunication);

    }

    /**
     * Test the querying of rooms with only a start or (exclusive) end time.
     */
    @Test
    void queryRoomsOnlyStartOrEndTime() {
        List<Room> roomResultList = Arrays.asList(room1, room2, room3, room4);
        List<Room> roomTestList1 = spyController.queryRooms(-1, -1,
                "", LocalDateTime.now().toString(), "", token);
        List<Room> roomTestList2 = spyController.queryRooms(-1, -1,
                "", "", LocalDateTime.now().toString(), token);

        assertEquals(roomResultList, roomTestList1);
        assertEquals(roomResultList, roomTestList2);
        verify(roomRepo, times(2)).findAll();
        verify(equipmentRepo, never()).findAllByEquipmentName("");
        verifyNoInteractions(reservationCommunication);

    }

    /**
     * Test the querying of rooms with a timeslot.
     */
    @Test
    void queryRoomsOnlyWithTimeslot() {
        String start = "2022-01-09T14:22:23.643606500";
        String end = "2022-01-09T17:22:23.643606500";

        List<Room> roomResultList = List.of(room3, room4);
        List<Room> roomTestList = spyController.queryRooms(-1, -1,
                "", start, end, token);

        assertEquals(roomResultList, roomTestList);
        verify(roomRepo, times(1)).findAll();
        verify(roomRepo, times(1)).findAllById(Arrays.asList(6L, 7L));
        verify(equipmentRepo, never()).findAllByEquipmentName("");
        verify(spyController, times(1)).getRoomsInTimeslot(any(), any(), any(), any());
    }
}