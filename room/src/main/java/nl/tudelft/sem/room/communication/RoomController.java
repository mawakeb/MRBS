package nl.tudelft.sem.room.communication;


import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.repository.BuildingRepository;
import nl.tudelft.sem.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

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
    public boolean checkAvailable(@RequestParam long roomId, @RequestParam LocalTime start, @RequestParam LocalTime end ) {

        //get room from the repository
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
