package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.EquipmentRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class RoomController {

    private final transient RoomRepository roomRepo;
    private final transient BuildingRepository buildingRepo;
    private final transient EquipmentRepository equipmentRepo;
    protected static Gson gson = new Gson();

    @Autowired
    public RoomController(RoomRepository roomRepo, BuildingRepository buildingRepo, EquipmentRepository equipmentRepo) {
        this.roomRepo = roomRepo;
        this.buildingRepo = buildingRepo;
        this.equipmentRepo = equipmentRepo;
    }

    @GetMapping("")
    public String testMethod() {
        return "Hello_Room!";
    }

    @GetMapping("checkAvailable")
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
    @GetMapping("getById")
    public Room getById(@RequestParam Long roomId) {
        return roomRepo.findById(roomId).orElse(null);
    }


    @GetMapping("queryRooms")
    public List<Room> queryRooms(@RequestParam int capacity, @RequestParam long buildingId,
                                 @RequestParam String equipmentName,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime) {
        // lists to be used
        List<Room> rooms = roomRepo.findAll();
        List<Room> filteredRooms = new ArrayList<>();
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        Set<Long> roomsWithEquipment = equipmentRepo.findAllByEquipmentName(equipmentName)
                .stream().map(EquipmentInRoom::getRoomId).collect(Collectors.toSet());

        // add the rooms that fit the first 3 characteristics to a new list
        for (Room r : rooms) {
            if ((capacity != -1 || r.getCapacity() >= capacity) &&
                    (buildingId != -1 || r.getBuildingId() == buildingId) &&
                    (!Objects.equals(equipmentName, "") || roomsWithEquipment.contains(r.getId()))) {
                filteredRooms.add(r);
            }
        }

        // do the last search characteristic timeslot availability
        if (!Objects.equals(startTime, "") && !Objects.equals(endTime, "")) {
            // get the rooms within the timeslot
            return roomRepo.findAllById(ReservationCommunication.getRoomsInTimeslot(
                    filteredRooms.stream().map(Room::getId).collect(Collectors.toList()),
                    startTime, endTime));
        } else {
            return filteredRooms;
        }

    }
}
