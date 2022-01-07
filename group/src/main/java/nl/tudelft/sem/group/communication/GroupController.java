package nl.tudelft.sem.group.communication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group")
public class GroupController {

    @GetMapping("")
    public String testMethod() {
        return "Hello_Group!";
    }
}
