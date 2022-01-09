package nl.tudelft.sem.group.communication;

import nl.tudelft.sem.group.entity.Group;
import nl.tudelft.sem.group.exception.GroupNotFoundException;
import nl.tudelft.sem.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final transient GroupRepository groupRepository;

    @Autowired
    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Gets the id-s of all secretaries of a certain research group.
     * @param groupId - the id of the research group for which we want to find all secretary id-s.
     * @return - a list of secretary id-s.
     * @throws GroupNotFoundException
     */
    @GetMapping("getSecretariesIdsOfAGroup")
    public List<Long> getSecretariesIds(@RequestParam Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            throw new GroupNotFoundException("The research group was not found.");
        } else {
            return group.getSecretaryIds();
        }
    }

    /**
     * Check if a given secretary is a secretary of a certain research group.
     * @param secretaryId - the id of the secretary for which we want to check.
     * @param groupId - the id of the research group for which we want to check.
     * @return - true if the given secretary is actually a secretary of the given research group, false otherwise.
     * @throws GroupNotFoundException
     */
    @GetMapping("isSecretaryOfGroup")
    public boolean isSecretaryOfGroup(Long secretaryId, Long groupId) throws GroupNotFoundException {
        List<Long> secretaries = getSecretariesIds(groupId);
        return secretaries.contains(secretaryId);
    }

    /**
     * Check if a user is a part of a certain research group.
     * @param userId - the id of the user for which we want to check.
     * @param groupId - the id of the research group for which we want to check.
     * @return - true if the given user is actually a part of the given research group, false otherwise.
     * @throws GroupNotFoundException
     */
    @GetMapping("isInGroup")
    public boolean isInGroup(Long userId, Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            throw new GroupNotFoundException("The research group was not found.");
        } else {
            return group.getMembersIds().contains(userId);
        }
    }

    /**
     * Checks if there is at least one employee in group 1 that is also in group 2.
     * @param groupId1 - the id of the first group.
     * @param groupId2 - the id of the second group.
     * @return - true if there is at least one employee that is both in group 1 AND in group 2, false otherwise.
     * @throws GroupNotFoundException
     */
    @GetMapping("overlap")
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean overlap(Long groupId1, Long groupId2) throws GroupNotFoundException {
        Group group1 = groupRepository.findById(groupId1).orElse(null);
        Group group2 = groupRepository.findById(groupId2).orElse(null);

        if (group1 == null || group2 == null) {
            throw new GroupNotFoundException("The groups were not found.");
        }

        List<Long> members1 = group1.getMembersIds();
        List<Long> members2 = group2.getMembersIds();

        for(Long member : members1)
        {
            if(members2.contains(member)) return true;
        }
        return false;
    }

    /**
     * Creates a research group.
     * @param secretaryIds - a list of all the secretary id-s that would be in that research group.
     * @param membersIds - a list of all the members id-s that would be in that research group.
     * @return - a message to the user which either confirms the research group was saved successfully
     * OR if the user does not have the rights to create a research group it lets them know.
     */
    @PostMapping("createGroup")
    public String createGroup(@RequestParam List<Long> secretaryIds,
                              @RequestParam List<Long> membersIds,
                              @RequestHeader("Authorization") String token) {
        if (getCurrentUserType(token).equals("ADMIN")) {
            for (Long l : secretaryIds) {
                if (!getUserType(l, token).equals("SECRETARY")) {
                    setUserType(l, "SECRETARY", token);
                }
            }

            Group group = new Group(secretaryIds, membersIds);
            groupRepository.save(group);
            return "The research group has been saved successfully!";
        }
        return "You do not have the permission to create a group in the database.";
    }


    /**
     * Get the type of user with the auth token.
     * Added to allow unit testing.
     *
     * @param token the authentication token of the user
     * @return the type of the user
     */
    public String getCurrentUserType(String token) {
        return UserCommunication.getCurrentUserType(token);
    }

    /**
     * Get the type of user with the id.
     * Added to allow unit testing.
     *
     * @param id the id of the requested user
     * @param token the authentication token of the current user
     * @return the type of the user
     */
    public String getUserType(long id, String token) {
        return UserCommunication.getUserType(id, token);
    }

    /**
     * Set the type of user with the id.
     * Added to allow unit testing.
     *
     * @param id the id of the requested user
     * @param type the new type for the requested user
     * @param token the authentication token of the current user
     * @return a status message
     */
    public String setUserType(long id, String type, String token) {
        return UserCommunication.setUserType(id, type, token);
    }

}
