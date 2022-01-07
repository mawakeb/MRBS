package nl.tudelft.sem.user.communication;

import java.util.List;
import java.util.ArrayList;

import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class UserController {

    private final transient UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    @GetMapping("")
    public String testMethod() {
        return "Hello_User!";
    }
    */

    @GetMapping("getUserType")
    public String getUserType(Long id) {
        return userRepository.findById(id).get().getType().toString();
    }

    @GetMapping("setUserType")
    public String setUserType(Long id, String type) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            user.setType(Type.valueOf(type));

            userRepository.save(user);
            return "User type changed successfully";
        }
        else return "User not found";
    }

    @GetMapping("getCurrentUserType")
    public String getCurrentUserType()
    {
        return "EMPLOYEE";
    }

    @GetMapping("getCurrentUserID")
    public Long getCurrentUserID()
    {
        return (long) 0;
    }

    @GetMapping("getTeamMemberIDs")
    public List getBuildingOpeningHours(@RequestParam(value = "secretaryUserID") Long secretaryUserID) {
        ArrayList<Long> result = new ArrayList<Long>();
        result.add((long) 1234);
        return result;
    }
}
