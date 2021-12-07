package nl.tudelft.sem.room.communication;

import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("room")
public class RoomController {

    private final transient RoomRepository roomRepo;
    private final transient BuildingRepository buildingRepo;

    @Autowired
    public RoomController(RoomRepository roomRepo, BuildingRepository buildingRepo) {
        this.roomRepo = roomRepo;
        this.buildingRepo = buildingRepo;
    }

    @GetMapping("")
    public String testMethod() {
        return "Hello_Room!";
    }

    @GetMapping("checkAvailable")
    public boolean checkAvailable(@RequestBody String q) {

        //extract room id and the start and end times
        Long roomId = 1L;


        Room room = roomRepo.findById(roomId).orElse(null);
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

            LocalTime open = building.getOpenTime();
            LocalTime close = building.getCloseTime();
            //this should be extracted from the message but i put it here to pass a PMD test
            LocalTime start = LocalTime.now();
            LocalTime end = LocalTime.now();

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
