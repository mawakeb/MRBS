package nl.tudelft.sem.room.communication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("room")
public class RoomController {

    @GetMapping("getBuildingOpeningHours")
    public String getBuildingOpeningHours(@RequestParam(value = "roomID") int roomID) {
        return "hi";
    }
}
