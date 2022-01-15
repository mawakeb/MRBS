package nl.tudelft.sem.room.communication;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.EquipmentRepository;
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
    private final transient EquipmentRepository equipmentRepo;
    //PMD: AvoidDuplicateLiterals
    private final transient String authorization = "Authorization";

    /**
     * Constructor of the room controller to autowire repositories.
     *
     * @param roomRepo repository of rooms
     * @param equipmentRepo repository of equipments
     */
    @Autowired
    public RoomController(RoomRepository roomRepo,
                          EquipmentRepository equipmentRepo) {
        this.roomRepo = roomRepo;
        this.equipmentRepo = equipmentRepo;

    }

    /**
     * Check if the room is available.
     *
     * @param roomId id of room to check
     * @return true if room is available, false if otherwise
     */
    @GetMapping("/checkAvailable")
    public boolean checkAvailable(@RequestParam long roomId,
                                  @RequestParam LocalTime start,
                                  @RequestParam LocalTime end) {
        Room room = roomRepo.findById(roomId);
        if (room == null) {
            return false;
        }

        boolean available = !room.isUnderMaintenance();

        return available;
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
     * Return buildingId of the room.
     *
     * @param roomId room to get the building id
     * @return building id of the room
     */
    @GetMapping("/getBuildingId")
    public long getBuildingId(@RequestParam Long roomId) {
        Room room = roomRepo.findById(roomId).orElse(null);
        return room.getBuildingId();
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

        if (checkAdmin(token)) {
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
        if (checkAdmin(token)) {
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
        if (checkAdmin(token)) {
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
     * Check whether given user is an admin.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return true if the user is an admin, false otherwise.
     */
    public boolean checkAdmin(String token) {
        return UserCommunication.getUserType(token).equals("ADMIN");
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


}

