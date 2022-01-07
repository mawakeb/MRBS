package nl.tudelft.sem.group.communication;

import nl.tudelft.sem.group.entity.Group;
import nl.tudelft.sem.group.exception.GroupNotFoundException;
import nl.tudelft.sem.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class GroupController {

    private final transient GroupRepository groupRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("getSecretaryIdOfAGroup")
    public Long getSecretaryId(@RequestParam Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new GroupNotFoundException("Group was not found.");
        } else {
            return group.getSecretaryId();
        }
    }

    @GetMapping("getSecretariesIdsOfAGroup")
    public List<Long> getSecretariesId(@RequestParam Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            throw new GroupNotFoundException("Group was not found.");
        }

        List<Long> secretariesIds = new ArrayList<Long>();
        List<Long> membersIds = group.getMembersIds();

        for(int i = 0; i < membersIds.size(); i++) {
            String memberType = UserCommunication.getUserType(membersIds.get(i));

            if (memberType.equals("SECRETARY")) {
                secretariesIds.add(membersIds.get(i));
            }
        }

        return secretariesIds;
    }

    @PostMapping("createGroup")
    public String createGroup(@RequestParam Long secretaryId, @RequestParam List<Long> membersIds) {
        if (UserCommunication.getCurrentUserType().equals("ADMIN")) {
            if (!UserCommunication.getUserType(secretaryId).equals("SECRETARY")) {
                UserCommunication.setUserType(secretaryId, "SECRETARY");
            }
            Group group = new Group(secretaryId, membersIds);
            groupRepository.save(group);
            return "Group has been saved successfully";
        }
        return "You do not have the permission to create a group in the database";
    }
}
