package nl.tudelft.sem.room.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("room")
public class RoomController {

    private final transient RoomRepository roomRepo;
    private final transient BuildingRepository buildingRepo;
    protected static Gson gson = new Gson();

    @Autowired
    public RoomController(RoomRepository roomRepo, BuildingRepository buildingRepo) {
        this.roomRepo = roomRepo;
        this.buildingRepo = buildingRepo;
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

        if (available){
            Long buildingId = room.getId();
            Building building = buildingRepo.findById(buildingId).orElse(null);

            if (building == null){
                //do something but this shouldn't happen
                return false;
            }

            //get the opening hours of the building
            LocalTime open = building.getOpenTime();
            LocalTime close = building.getCloseTime();

            //get the timeslot of the reservation
            LocalTime start = LocalTime.parse(list.get(1));
            LocalTime end = LocalTime.parse(list.get(2));

            if (start.isAfter(open) && end.isBefore(close)){
                return true;
            }
        } else {
            //inform user that the room is under maintenance
            return false;
        }

        return false;
    }
}
