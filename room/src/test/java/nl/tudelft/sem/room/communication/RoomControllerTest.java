package nl.tudelft.sem.room.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.EquipmentRepository;
import nl.tudelft.sem.room.repository.NoticeRepository;
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

    @Mock
    transient RoomRepository roomRepo;
    @Mock
    transient BuildingRepository buildingRepo;
    @Mock
    transient EquipmentRepository equipmentRepo;
    @Mock
    transient NoticeRepository noticeRepo;
    @Mock
    transient UserCommunication userCommunication;
    @Mock
    transient ReservationCommunication reservationCommunication;

    private transient RoomController controller;
    private transient RoomController spyController;
    private transient List<Room> roomList;
    private transient Building building;
    private transient List<RoomNotice> noticeList;
    private transient String token = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RoomController(roomRepo, buildingRepo, equipmentRepo, noticeRepo);
        roomList = List.of(new Room(1L, "meeting room1", 10L, 20),
                new Room(2L, "meeting room2", 10L, 20),
                new Room(3L, "meeting room3", 10L, 20));
        building = new Building(10L, "building",
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        noticeList = List.of(new RoomNotice(1L, 101L, "door is broken"),
                new RoomNotice(1L, 102L, "problems with computer"),
                new RoomNotice(2L, 103L, "window is broken"));

        //mock RoomRepository
        lenient().when(roomRepo.findById(1L)).thenReturn(roomList.get(0));

        //mock BuildingRepository
        lenient().when(buildingRepo.findById(10L)).thenReturn(building);

        //mock NoticeRepository
        lenient().when(noticeRepo.findByRoomId(1L)).thenReturn(List.of(noticeList.get(0), noticeList.get(1)));

        //mock static communication methods
        spyController = Mockito.spy(controller);
        lenient().doReturn("ADMIN").when(spyController).getRole(token);
        lenient().doReturn(200L).when(spyController).getUserId(token);
        lenient().doReturn(true).when(spyController)
                .checkUserToReservation(200L, 105L, token);
        lenient().doReturn(1L).when(spyController)
                .getRoomWithReservation(105L, token);

    }

    @Test
    void testMethod() {
        assertEquals("Hello_Room!", controller.testMethod());
    }

    @Test
    void checkAvailable() {
        boolean actual = controller.checkAvailable(1L,
                LocalTime.of(12, 0),
                LocalTime.of(14, 0));
        assertTrue(actual);
    }

    @Test
    void checkNotAvailable() {
        boolean actual1 = controller.checkAvailable(1L,
                LocalTime.of(15, 0),
                LocalTime.of(19, 0));
        boolean actual2 = controller.checkAvailable(1L,
                LocalTime.of(19, 0),
                LocalTime.of(23, 0));
        assertFalse(actual1);
        assertFalse(actual2);
    }


    @Test
    void getById() {
        assertEquals(roomList.get(0), controller.getById(1L));
    }

    @Test
    void queryRooms() {
    }

    @Test
    void leaveNotice() {
        String success = spyController.leaveNotice("token", 105L, "projector not working");
        assertEquals("Notice saved successfully", success);
        verify(noticeRepo, times(1)).save(any(RoomNotice.class));
    }

    @Test
    void getNotice() {
        List<RoomNotice> actual = spyController.getNotice(token, 1L);
        List<RoomNotice> expected = List.of(noticeList.get(0), noticeList.get(1));
        assertEquals(expected, actual);
    }

    @Test
    void changeStatus() {
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
    void createBuilding() {
        when(buildingRepo.findById(11L)).thenReturn(null);
        String success = spyController.createBuilding(token, 11L, "building2",
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        assertEquals("Building has been saved successfully", success);
        verify(buildingRepo, times(1)).save(any(Building.class));
    }

    @Test
    void createEquipment() {
        String success = spyController.createEquipment(token, 1L, "chair");
        assertEquals("Equipment has been saved successfully", success);
        verify(equipmentRepo, times(1)).save(any(EquipmentInRoom.class));
    }
}