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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("room")
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

    @PostMapping("checkAvailable")
    public boolean checkAvailable(@RequestBody String q) {

        //extract list from the message
        List<String> list = gson.fromJson(q, new TypeToken<List<String>>() {}.getType());

        //get room from the repository
        long roomId = Long.parseLong(list.get(0));
        Room room = roomRepo.findById(roomId);
        if (room == null) {
            //inform user that the room was not found
            return false;
        }

        boolean available = room.isUnderMaintenance();

        if (available) {
            Long buildingId = room.getId();
            Building building = buildingRepo.findById(buildingId).orElse(null);

            if (building == null){
                //do something but this shouldn't happen
                return false;
            }

            //get the timeslot of the reservation
            LocalTime start = LocalTime.parse(list.get(1));
            LocalTime end = LocalTime.parse(list.get(2));

            // return whether the building is open or not for the timeslot
            return building.isOpen(start, end);
        } else {
            //inform user that the room is under maintenance
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
    public List<Room> queryRooms(@RequestBody String q) {
        //extract list from the message
        List<String> list = gson.fromJson(q, new TypeToken<List<String>>() {}.getType());

        // lists for the rooms
        List<Room> rooms = roomRepo.findAll();
        List<Room> filteredRooms = new ArrayList<>();

        // parameters to potentially check for (given default values)
        int capacity = 1;
        long buildingId = 1;
        List<Long> roomsWithEquipment = new ArrayList<>();

        // By using the property that a prime number is only divisible by 1 and itself
        // I can make a number that instantly allows me to know what parameters were given
        int givenParameterChecksum = 1;
        // multiply the checksum by a prime number to check later
        // and give the corresponding parameter the right value
        if (!Objects.equals(list.get(0), "")) {
            capacity = Integer.parseInt(list.get(0));

            givenParameterChecksum *= 2;
        }
        if (!Objects.equals(list.get(1), "")) {
            buildingId = Long.parseLong(list.get(1));

            givenParameterChecksum *= 3;
        }
        if (!Objects.equals(list.get(2), "")) {
            List<EquipmentInRoom> equipmentInRooms = equipmentRepo.findAllByEquipmentName(list.get(2));
            for (EquipmentInRoom eir : equipmentInRooms) {
                roomsWithEquipment.add(eir.getRoomId());
            }

            givenParameterChecksum *= 5;
        }

        // add the rooms that fit the first 3 characteristics to a new list
        for (Room r : rooms) {
            if ((givenParameterChecksum % 2 != 0 || r.getCapacity() == capacity) &&
                    (givenParameterChecksum % 3 != 0 || r.getBuildingId() == buildingId) &&
                    (givenParameterChecksum % 5 != 0 || roomsWithEquipment.contains(r.getId()))) {
                filteredRooms.add(r);
            }
        }

        if (!Objects.equals(list.get(3), "") && !Objects.equals(list.get(4), "")) {
            return filteredRooms;
        } else {
            return filteredRooms;
        }

    }
}
