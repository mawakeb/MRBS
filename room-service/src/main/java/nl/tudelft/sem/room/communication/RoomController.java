package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.entity.RoomNotice;
import nl.tudelft.sem.room.exception.RoomNotFoundException;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.EquipmentRepository;
import nl.tudelft.sem.room.repository.NoticeRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


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

        if (!available) {
            Long buildingId = room.getBuildingId();
            Building building = buildingRepo.findById(buildingId).orElseGet(null);

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
        List<Room> filteredRooms = new ArrayList<>();
        List<Room> rooms;
        if (equipmentName.isEmpty()) {
            rooms = roomRepo.findAll();
        } else {
            rooms = roomRepo.findAllById(equipmentRepo.findAllByEquipmentName(equipmentName));
        }

        // now filter for the capacity and the building
        for (Room r : rooms) {
            if ((r.getCapacity() >= capacity)
                    && (buildingId == -1 || r.getBuildingId() == buildingId)) {
                filteredRooms.add(r);
            }
        }

        // do the last search characteristic timeslot availability
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            // get the rooms within the timeslot
            return roomRepo.findAllById(getRoomsInTimeslot(
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

        long userId = getUserId(token);
        //if the user is the owner of the reservation, save a new notice in NoticeRepository
        if (checkUserToReservation(userId, reservationId, token)) {
            long roomId = getRoomWithReservation(reservationId, token);
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
    public List<RoomNotice> getNotice(@RequestHeader(authorization) String token,
                                        @RequestParam long roomId) {

        if (getRole(token).equals(admin)) {
            List<RoomNotice> notices = noticeRepo.findByRoomId(roomId);
            return notices;
        } else {
            throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "You do not have access to maintenance notices");
        }
    }

    /**
     * Change the maintenance status of the room.
     *
     * @param token authentication token of the user
     * @param roomId id of the room to change the status
     * @param status the new status
     * @return a success message if the process was successful, return an error message otherwise
     */
    @PostMapping("/changeStatus")
    public String changeStatus(@RequestHeader(authorization) String token,
                               @RequestParam long roomId,
                               @RequestParam boolean status) {

        if (getRole(token).equals(admin)) {
            Room room = roomRepo.findById(roomId);
            if (room != null) {
                room.setUnderMaintenance(status);
                roomRepo.save(room);
                return "Status changed successfully";
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room was not found");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have access to room maintenance");
        }
    }

    /**
     * Allow admins to create a room in the database.
     *
     * @param token authentication token of the user
     * @param id id of the room
     * @param name name of the room
     * @param buildingId id of the building that the room is in
     * @param capacity capacity of the room
     * @return a success message if the process was successful, return an error message otherwise
     */
    @PostMapping("/createRoom")
    public String createRoom(@RequestHeader(authorization) String token,
                             @RequestParam long id,
                             @RequestParam String name,
                             @RequestParam long buildingId,
                             @RequestParam int capacity) {
        if (getRole(token).equals(admin)) {
            if (roomRepo.findById(id) == null) {
                Room room = new Room(id, name, buildingId, capacity);
                roomRepo.save(room);
                return "Room has been saved successfully";
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "There is existing room with same id");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have access to creating rooms");
        }
    }

    /**
     * Allow admins to create a building in the database.
     *
     * @param token authentication token of the user
     * @param id id of the building
     * @param name name of the building
     * @param openTime opening time of the building
     * @param closeTime closing time of the building
     * @return a success message if the process was successful, return an error message otherwise
     */
    @PostMapping("/createBuilding")
    public String createBuilding(@RequestHeader(authorization) String token,
                                 @RequestParam long id,
                                 @RequestParam String name,
                                 @RequestParam LocalTime openTime,
                                 @RequestParam LocalTime closeTime) {
        if (getRole(token).equals(admin)) {
            if (buildingRepo.findById(id) == null) {
                Building building = new Building(id, name, openTime, closeTime);
                buildingRepo.save(building);
                return "Building has been saved successfully";
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "There is existing building with same id");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have access to creating buildings");
        }
    }

    /**
     * Allow admins to create equipment in the database.
     *
     * @param token authentication token of the user
     * @param roomId id of the room that the equipment is in
     * @param equipmentName name of the equipment
     * @return a success message if the process was successful, return an error message otherwise
     */
    @GetMapping("/createEquipment")
    public String createEquipment(@RequestHeader(authorization) String token,
                                  @RequestParam Long roomId,
                                  @RequestParam String equipmentName) {
        if (getRole(token).equals(admin)) {
            if (roomRepo.findById(roomId) != null) {
                EquipmentInRoom equipment = new EquipmentInRoom(roomId, equipmentName);
                equipmentRepo.save(equipment);
                return "Equipment has been saved successfully";
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Room was not found");
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You do not have access to creating equipments");
        }
    }

    /**
     * Get role of user with given authentication token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return the role of the user
     */
    public String getRole(String token) {
        return UserCommunication.getUserType(token);
    }

    /**
     * Get id of user with the auth token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return id of the user
     */
    public long getUserId(String token) {
        return UserCommunication.getUserId(token);
    }

    /**
     * Check if given userId matches the owner of the reservation.
     * Added to allow unit testing.
     *
     * @param userId id of the user
     * @param reservationId id of the reservation
     * @param token authentication token of the user
     * @return true if user is the owner of the reservation
     */
    public boolean checkUserToReservation(long userId,
                                          long reservationId, String token) {
        return ReservationCommunication
                .checkUserToReservation(userId, reservationId, token);
    }

    /**
     * Get rooms available within the given timeslot.
     * Added to allow unit testing.
     *
     * @param rooms list of rooms
     * @param startTime start time
     * @param endTime end time
     * @param token authentication token of the user
     * @return list of available rooms
     */
    public List<Long> getRoomsInTimeslot(List<Long> rooms,
                                         String startTime,
                                         String endTime,
                                         String token) {
        return ReservationCommunication
                .getRoomsInTimeslot(rooms, startTime, endTime, token);
    }

    /**
     * Get room id with the reservation id.
     * Added to allow unit testing.
     *
     * @param reservationId id of the reservation
     * @param token authentication token of the user
     * @return room id
     */
    public long getRoomWithReservation(long reservationId,
                                       String token) {
        return ReservationCommunication
                .getRoomWithReservation(reservationId, token);
    }
}

