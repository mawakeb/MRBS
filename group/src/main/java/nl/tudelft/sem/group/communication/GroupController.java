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

    @GetMapping("getSecretariesIdsOfAGroup")
    public List<Long> getSecretariesId(@RequestParam Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            throw new GroupNotFoundException("Group was not found.");
        } else {
            return group.getSecretaryIds();
        }
    }

    @GetMapping("isSecretaryOfGroup")
    public boolean isSecretaryOfGroup(Long secretaryId, Long groupId)
    {
        List<Long> secretries = getSecretariesId(groupId);
        return secretries.contains(secretaryId);
    }

    @GetMapping("isSecretaryOfUser")
    public boolean isSecretaryOfUser(Long secretaryId, Long employeeId)
    {
        return false;
    }

    @GetMapping("isInGroup")
    public boolean isInGroup(Long userId, Long groupId)
    {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            throw new GroupNotFoundException("Group was not found.");
        } else {
            return group.getMembersIds().contains(userId);
        }
    }

    //returns true iff there is at least one employee in group 1 that is also in group 2
    @GetMapping("overlap")
    public boolean overlap(Long groupId1, Long groupId2)
    {
        Group group1 = groupRepository.findById(groupId1).orElse(null);
        Group group2 = groupRepository.findById(groupId2).orElse(null);

        if (group1 == null || group2 == null) {
            throw new GroupNotFoundException("Groups were not found.");
        }

        List<Long> members1 = group.getMembersIds();
        List<Long> members2 = group.getMembersIds();

        for(member : members1)
        {
            if(members2.contains(member)) return true;
        }
        return false;
    }

    @PostMapping("createGroup")
    public String createGroup(@RequestParam List<Long> secretaryIds, @RequestParam List<Long> membersIds) {
        if (UserCommunication.getCurrentUserType().equals("ADMIN")) {
            for (Long l : secretaryIds) {
                if (!UserCommunication.getUserType(l).equals("SECRETARY")) {
                    UserCommunication.setUserType(l, "SECRETARY");
                }
            }
            Group group = new Group(secretaryIds, membersIds);
            groupRepository.save(group);
            return "Group has been saved successfully";
        }
        return "You do not have the permission to create a group in the database";
    }
}
