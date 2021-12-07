package nl.tudelft.sem.room.communication;

import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("room")
public class RoomController {

    private final transient RoomRepository roomRepo;

    @Autowired
    public RoomController(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    @GetMapping("")
    public String testMethod() {
        return "Hello_Room!";
    }

    @GetMapping("checkAvailable")
    public boolean checkAvailable(@RequestBody String q) {

        //extract room id and the start and end times
        //Long roomId = 1L;
        //LocalTime start;
        //LocalTime end;

        /*
        Room room = roomRepo.findById(roomId);
        boolean available = room.isUnderMaintenance();

        if (available){
            Long buildingId = room.getId();
            Building building = buildingRepo.findById(buildingId);
            LocalTime open = building.getOpenTime();
            LocalTime close = building.getCloseTime();
            if (start.isAfter(open) && end.isBefore(close)){
                return true;
            }
        } else {
            //return message that the room is under maintenance
        } */

        return true;
    }
}
