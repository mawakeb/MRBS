package nl.tudelft.sem.user.communication;

import java.util.List;
import java.util.ArrayList;
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

    /*
    @GetMapping("")
    public String testMethod() {
        return "Hello_User!";
    }
    */

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
