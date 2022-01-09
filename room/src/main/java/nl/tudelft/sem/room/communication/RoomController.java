package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.EquipmentRepository;
import nl.tudelft.sem.room.repository.NoticeRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/room")
public class RoomController {

    private final transient RoomRepository roomRepo;
    private final transient BuildingRepository buildingRepo;
    private final transient EquipmentRepository equipmentRepo;
    private final transient NoticeRepository noticeRepo;
    protected static Gson gson = new Gson();
    //PMD: AvoidDuplicateLiterals
    private final transient String admin = "ADMIN";
    private final transient String authorization = "Authorization";

    /**
     * Constructor of the room controller to autowire repositories.
     *
     * @param roomRepo repository of rooms
     * @param buildingRepo repository of buildings
     * @param equipmentRepo repository of equipments
     * @param noticeRepo repository of room notices
     */
    @Autowired
    public RoomController(RoomRepository roomRepo,
                          BuildingRepository buildingRepo,
                          EquipmentRepository equipmentRepo,
                          NoticeRepository noticeRepo) {
        this.roomRepo = roomRepo;
        this.buildingRepo = buildingRepo;
        this.equipmentRepo = equipmentRepo;
        this.noticeRepo = noticeRepo;

    }

    /**
     * Test method.
     *
     * @return return "Hello_Room!"
     */
    @GetMapping("")
    public String testMethod() {
        return "Hello_Room!";
    }

    /**
     * Check if the room is available at given time.
     *
     * @param roomId id of room to check
     * @param start start of the timeslot to check
     * @param end end of the timeslot to check
     * @return true if room is available, false if otherwise
     */
    @GetMapping("/checkAvailable")
    public boolean checkAvailable(@RequestParam long roomId,
                                  @RequestParam LocalTime start,
                                  @RequestParam LocalTime end) {
        Room room = roomRepo.findById(roomId);
        if (room == null) {
            // inform user that the room was not found
            return false;
        }

        boolean available = room.isUnderMaintenance();

        if (available) {
            Long buildingId = room.getId();
            Building building = buildingRepo.findById(buildingId).orElse(null);

            if (building == null) {
                // do something but this shouldn't happen
                return false;
            }

            // return whether the building is open or not for the timeslot
            return building.isOpen(start, end);
        } else {
            // inform user that the room is under maintenance
            return false;
        }
    }

    /**
     * Get room by id.
     *
     * @param roomId the room id
     * @return the room
     */
    @GetMapping("/getById")
    public Room getById(@RequestParam Long roomId) {
        return roomRepo.findById(roomId).orElse(null);
    }


    /**
     * Returns rooms that are available in the given conditions.
     *
     * @param capacity minimum capacity of the room required
     * @param buildingId id of the wanted building
     * @param equipmentName required equipment
     * @param startTime start time of the timeslot
     * @param endTime end time of the timeslot
     * @param token authentication token of the room
     * @return list of rooms that satisfy the conditions
     */
    @GetMapping("/queryRooms")
    public List<Room> queryRooms(@RequestParam int capacity, @RequestParam long buildingId,
                                 @RequestParam String equipmentName,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 @RequestHeader(authorization) String token) {
        // lists to be used
        List<Room> rooms = roomRepo.findAll();
        List<Room> filteredRooms = new ArrayList<>();
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        Set<Long> roomsWithEquipment = equipmentRepo.findAllByEquipmentName(equipmentName)
                .stream().map(EquipmentInRoom::getRoomId).collect(Collectors.toSet());

        // add the rooms that fit the first 3 characteristics to a new list
        for (Room r : rooms) {
            if ((capacity != 0 || r.getCapacity() >= capacity)
                    && (buildingId != 0 || r.getBuildingId() == buildingId)
                    && (!Objects.equals(equipmentName, "")
                    || roomsWithEquipment.contains(r.getId()))) {
                filteredRooms.add(r);
            }
        }

        // do the last search characteristic timeslot availability
        if (!Objects.equals(startTime, "") && !Objects.equals(endTime, "")) {
            // get the rooms within the timeslot
            return roomRepo.findAllById(ReservationCommunication.getRoomsInTimeslot(
                    filteredRooms.stream().map(Room::getId).collect(Collectors.toList()),
                    startTime, endTime, token));
        } else {
            return filteredRooms;
        }

    }

    /**
     * Leave a message in the database regarding maintenance of the room.
     *
     * @param token authentication token of the user
     * @param reservationId id of the reservation which the user faced problems in.
     * @param message the actual message
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @PostMapping("/leaveNotice")
    public String leaveNotice(@RequestHeader(authorization) String token,
                              @RequestParam long reservationId,
                              @RequestParam String message) {

        long userId = UserCommunication.getUserId(token);
        //if the user is the owner of the reservation, save a new notice in NoticeRepository
        if (ReservationCommunication.checkUserToReservation(userId, reservationId, token)) {
            long roomId = ReservationCommunication.getRoomWithReservation(reservationId, token);
            RoomNotice notice = new RoomNotice(roomId, reservationId, message);
            noticeRepo.save(notice);
            return "Notice saved successfully";
        } else {
            return "There was an error";
        }
    }

    /**
     * Retrieve all the notices left for a room.
     *
     * @param token authentication token of the user
     * @param roomId if of the room to retrieve the messages
     * @return the list of RoomNotice
     */
    @GetMapping("/getNotice")
    public List<RoomNotice> leaveNotice(@RequestHeader(authorization) String token,
                                        @RequestParam long roomId) {

        if (UserCommunication.getUserType(token).equals(admin)) {
            List<RoomNotice> notices = noticeRepo.findByRoomId(roomId);
            return notices;
        }
        return null;
    }

    /**
     * Change the maintenance status of the room.
     *
     * @param token authentication token of the user
     * @param roomId id of the room to change the status
     * @param status the new status
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @PostMapping("/changeStatus")
    public String changeStatus(@RequestHeader(authorization) String token,
                               @RequestParam long roomId,
                               @RequestParam boolean status) {

        if (UserCommunication.getUserType(token).equals(admin)) {
            Room room = roomRepo.findById(roomId);
            if (room != null) {
                room.setUnderMaintenance(status);
                roomRepo.save(room);
                return "Status changed successfully";
            } else {
                return "Room was not found";
            }
        }
        return "You do not have the access to change the status";
    }

    /**
     * Allow admins to create a room in the database.
     *
     * @param token authentication token of the user
     * @param id id of the room
     * @param name name of the room
     * @param buildingId id of the building that the room is in
     * @param capacity capacity of the room
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @PostMapping("/createRoom")
    public String createRoom(@RequestHeader(authorization) String token,
                             @RequestParam long id,
                             @RequestParam String name,
                             @RequestParam long buildingId,
                             @RequestParam int capacity) {
        if (UserCommunication.getUserType(token).equals(admin)) {
            if (roomRepo.findById(id) == null) {
                Room room = new Room(id, name, buildingId, capacity);
                roomRepo.save(room);
                return "Room has been saved successfully";
            } else {
                return "There is a room with same id";
            }
        }
        return "You do not have access to creating rooms in the database";
    }

    /**
     * Allow admins to create a building in the database.
     *
     * @param token authentication token of the user
     * @param id id of the building
     * @param name name of the building
     * @param openTime opening time of the building
     * @param closeTime closing time of the building
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @PostMapping("/createBuilding")
    public String createBuilding(@RequestHeader(authorization) String token,
                                 @RequestParam long id,
                                 @RequestParam String name,
                                 @RequestParam LocalTime openTime,
                                 @RequestParam LocalTime closeTime) {
        if (UserCommunication.getUserType(token).equals(admin)) {
            if (buildingRepo.findById(id) == null) {
                Building building = new Building(id, name, openTime, closeTime);
                buildingRepo.save(building);
                return "Building has been saved successfully";
            } else {
                return "There is a building with same id";
            }
        }
        return "You do not have access to creating buildings in the database";
    }

    /**
     * Allow admins to create equipment in the database.
     *
     * @param token authentication token of the user
     * @param roomId id of the room that the equipment is in
     * @param equipmentName name of the equipment
     * @return a success message if the process was successfull, return an error message otherwise
     */
    @GetMapping("/createEquipment")
    public String createEquipment(@RequestHeader(authorization) String token,
                                  @RequestParam Long roomId,
                                  @RequestParam String equipmentName) {
        if (UserCommunication.getUserType(token).equals(admin)) {
            if (roomRepo.findById(roomId) != null) {
                EquipmentInRoom equipment = new EquipmentInRoom(roomId, equipmentName);
                equipmentRepo.save(equipment);
                return "Equipment has been saved successfully";
            } else {
                return "Room was not found";
            }
        }
        return "You do not have access to creating equipments in the database";
    }
}
