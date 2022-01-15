package nl.tudelft.sem.room.communication;

import java.time.LocalTime;
import nl.tudelft.sem.room.entity.Building;
import nl.tudelft.sem.room.repository.BuildingRepository;
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
@RequestMapping("/room/building")
public class BuildingController {

    private final transient BuildingRepository buildingRepo;

    @Autowired
    public BuildingController(BuildingRepository repo){
        this.buildingRepo = repo;
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
    public String createBuilding(@RequestHeader("Authorization") String token,
                                 @RequestParam long id,
                                 @RequestParam String name,
                                 @RequestParam LocalTime openTime,
                                 @RequestParam LocalTime closeTime) {
        if (UserCommunication.getUserType(token).equals("ADMIN")) {
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
     * Check if the building is available at given time.
     *
     * @param buildingId id of building to check
     * @param start start of the timeslot to check
     * @param end end of the timeslot to check
     * @return true if building is available, false if otherwise
     */
    @GetMapping("/checkAvailable")
    public boolean checkAvailable(@RequestParam long buildingId,
                                  @RequestParam LocalTime start,
                                  @RequestParam LocalTime end) {

        Building building = buildingRepo.findById(buildingId);

        if (building == null) {
            return false;
        }

        // return whether the building is open or not for the timeslot
        return building.isOpen(start, end);
    }
}
