package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.springframework.web.bind.annotation.*;


import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final transient RoomRepository roomRepo;
    private final transient BuildingRepository buildingRepo;
    private final transient EquipmentRepository equipmentRepo;
    private final transient NoticeRepository noticeRepo;
    protected static Gson gson = new Gson();
    //PMD: AvoidDuplicateLiterals
    private transient String admin = "ADMIN";

    @Autowired
    public RoomController(RoomRepository roomRepo, BuildingRepository buildingRepo, EquipmentRepository equipmentRepo, NoticeRepository noticeRepo) {
        this.roomRepo = roomRepo;
        this.buildingRepo = buildingRepo;
        this.equipmentRepo = equipmentRepo;
        this.noticeRepo = noticeRepo;

    }

    @GetMapping("")
    public String testMethod() {
        return "Hello_Room!";
    }

    @GetMapping("/checkAvailable")
    public boolean checkAvailable(@RequestParam long roomId, @RequestParam LocalTime start, @RequestParam LocalTime end) {
        Room room = roomRepo.findById(roomId);
        if (room == null) {
            // inform user that the room was not found
            return false;
        }

        boolean available = room.isUnderMaintenance();

        if (available) {
            Long buildingId = room.getId();
            Building building = buildingRepo.findById(buildingId).orElse(null);

            if (building == null){
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


    @GetMapping("/queryRooms")
    public List<Room> queryRooms(@RequestParam int capacity, @RequestParam long buildingId,
                                 @RequestParam String equipmentName,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime,
                                 @RequestHeader("Authorization") String token) {
        // lists to be used
        List<Room> rooms = roomRepo.findAll();
        List<Room> filteredRooms = new ArrayList<>();
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        Set<Long> roomsWithEquipment = equipmentRepo.findAllByEquipmentName(equipmentName)
                .stream().map(EquipmentInRoom::getRoomId).collect(Collectors.toSet());

        // add the rooms that fit the first 3 characteristics to a new list
        for (Room r : rooms) {
            if ((capacity != 0 || r.getCapacity() >= capacity) &&
                    (buildingId != 0 || r.getBuildingId() == buildingId) &&
                    (!Objects.equals(equipmentName, "") || roomsWithEquipment.contains(r.getId()))) {
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

    @PostMapping("/leaveNotice")
    public String leaveNotice(@RequestHeader("Authorization") String token, @RequestParam long reservationId, @RequestParam String message) {

        //if the user is the owner of the reservation, save a new notice in NoticeRepository
        if (UserCommunication.getUserType(token).equals(admin)) {
            long roomId = ReservationCommunication.getRoomWithReservation(reservationId);
            RoomNotice notice = new RoomNotice(roomId, reservationId, message);
            noticeRepo.save(notice);
            return "Notice saved successfully";
        } else {
            return "There was an error";
        }
    }

    @GetMapping("/getNotice")
    public List<RoomNotice> leaveNotice(@RequestHeader("Authorization") String token, @RequestParam long roomId) {

        if (UserCommunication.getUserType(token).equals(admin)) {
            List<RoomNotice> notices = noticeRepo.findByRoomId(roomId);
            return notices;
        }
        return null;
    }

    @PostMapping("/changeStatus")
    public String changeStatus(@RequestHeader("Authorization") String token, @RequestParam long roomId, @RequestParam boolean status) throws RoomNotFoundException {

        if (UserCommunication.getUserType(token).equals(admin)) {
            Room room = roomRepo.findById(roomId);
            if (room !=null){
                room.setUnderMaintenance(status);
                roomRepo.save(room);
                return "Status changed successfully";
            } else {
                return "Room was not found";
            }
        }
        return "You do not have the access to change the status";
    }

    @PostMapping("/createRoom")
    public String createRoom(@RequestHeader("Authorization") String token, @RequestParam long id, @RequestParam String name, @RequestParam long buildingId, @RequestParam int capacity){
        if (UserCommunication.getUserType(token).equals(admin)) {
            if(roomRepo.findById(id) == null){
                Room room = new Room(id, name, buildingId, capacity);
                roomRepo.save(room);
                return "Room has been saved successfully";
            } else {
                return "There is a room with same id";
            }
        }
        return "You do not have access to creating rooms in the database";
    }

    @PostMapping("/createBuilding")
    public String createBuilding(@RequestHeader("Authorization") String token, @RequestParam long id, @RequestParam String name, @RequestParam LocalTime openTime, @RequestParam LocalTime closeTime){
        if (UserCommunication.getUserType(token).equals(admin)) {
            if(buildingRepo.findById(id) == null){
                Building building = new Building(id, name, openTime, closeTime);
                buildingRepo.save(building);
                return "Building has been saved successfully";
            } else {
                return "There is a building with same id";
            }
        }
        return "You do not have access to creating buildings in the database";
    }

    @GetMapping("/createEquipment")
    public String createEquipment(@RequestHeader("Authorization") String token, @RequestParam Long roomId, @RequestParam String equipmentName){
        if (UserCommunication.getUserType(token).equals(admin)) {
            if(roomRepo.findById(roomId) != null){
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
