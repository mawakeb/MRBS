package nl.tudelft.sem.group.communication;

import nl.tudelft.sem.group.entity.Group;
import nl.tudelft.sem.group.exception.GroupNotFoundException;
import nl.tudelft.sem.group.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupControllerTest {

    private transient Group group;
    private transient Group otherGroup;
    private transient GroupController groupController;
    private transient GroupController spyController;
    private transient final String token = "token";
    private transient final String adminToken = "adminToken";

    @BeforeEach
    void setUp() {
        //Set up of the main group used for testing.
        Long groupId = 0L;
        List<Long> secretaries = new ArrayList<Long>();
        secretaries.add(1L);
        secretaries.add(23L);
        List<Long> members = new ArrayList<Long>();
        members.add(3L);
        group = new Group(secretaries, members);

        //Set up of the "other" group used for testing the overlap method.
        Long otherGroupId = 7L;
        List<Long> otherSecretaries = new ArrayList<Long>();
        otherSecretaries.add(25L);
        otherSecretaries.add(46L);
        List<Long> otherMembers = new ArrayList<Long>();
        otherMembers.add(52L);
        otherGroup = new Group(otherSecretaries, otherMembers);

        //Mocking the groupRepository.
        GroupRepository groupRepository = Mockito.mock(GroupRepository.class);

        //Mocking the calls to the database.
        lenient().when(groupRepository.findById(groupId)).thenReturn(Optional.ofNullable(group));
        lenient().when(groupRepository.findById(otherGroupId)).thenReturn(Optional.ofNullable(otherGroup));

        //Initializing the GroupController class to test.
        MockitoAnnotations.initMocks(this);
        groupController = new GroupController(groupRepository);

        //mock static communication methods
        spyController = Mockito.spy(groupController);
        lenient().doReturn("EMPLOYEE").when(spyController).getCurrentUserType(token);
        lenient().doReturn("ADMIN").when(spyController).getCurrentUserType(adminToken);
        lenient().doReturn("EMPLOYEE").when(spyController).getUserType(23L, adminToken);
        lenient().doReturn("SECRETARY").when(spyController).getUserType(29L, adminToken);
        lenient().doReturn("User type changed successfully").when(spyController)
                .setUserType(anyLong(), anyString(), anyString());
    }

    @Test
    void getSecretariesIds() {
        try {
            assertEquals(group.getSecretaryIds(), groupController.getSecretariesIds(0L));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSecretariesIdsGroupNotFound() {
        Exception exception = assertThrows(GroupNotFoundException.class, () -> groupController.getSecretariesIds(1L));

        String expectedMessage = "The research group was not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void isSecretaryOfGroup() {
        try {
            assertTrue(groupController.isSecretaryOfGroup(23L, 0L));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isSecretaryOfGroupNotFound() {
        Exception exception = assertThrows(GroupNotFoundException.class, () -> groupController.isSecretaryOfGroup(23L, 1L));

        String expectedMessage = "The research group was not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isInGroup() {
        try {
            assertTrue(groupController.isInGroup(3L,0L));
            assertTrue(groupController.isInGroup(23L, 0L));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isInGroupNotFound() {
        Exception exception = assertThrows(GroupNotFoundException.class, () -> groupController.isInGroup(23L, 1L));

        String expectedMessage = "The research group was not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void overlapTrue() {
        otherGroup.getMembersIds().add(23L);
        try {
            assertTrue(groupController.overlap(0L, 7L));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void overlapFalse() {
        try {
            assertFalse(groupController.overlap(0L, 7L));
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void overlapOneOfTheGroupsNotFound() {
        Exception exception = assertThrows(GroupNotFoundException.class, () -> groupController.overlap(0L, 1L));
        Exception exception2 = assertThrows(GroupNotFoundException.class, () -> groupController.overlap(2L, 7L));
        Exception exception3 = assertThrows(GroupNotFoundException.class, () -> groupController.overlap(2L, 3L));

        String expectedMessage = "The groups were not found.";
        String actualMessage = exception.getMessage();
        String actualMessage2 = exception2.getMessage();
        String actualMessage3 = exception3.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(actualMessage2.contains(expectedMessage));
        assertTrue(actualMessage3.contains(expectedMessage));
    }

    /**
     * Test the creation of a group without permission.
     */
    @Test
    void createGroupNoPermission() {
        String resultString = spyController.createGroup(Arrays.asList(23L, 29L),
                Arrays.asList(41L, 53L, 67L, 73L, 83L, 91L, 97L), token);

        verify(spyController, times(1)).getCurrentUserType(token);
        verify(spyController, times(0)).getUserType(anyLong(), any());
        verify(spyController, times(0)).setUserType(anyLong(), anyString(), any());
        assertEquals("You do not have the permission to create a group in the database.",
                resultString);
    }

    /**
     * Test the creation of a group with permission.
     */
    @Test
    void createGroupWithPermission() {
        String resultString = spyController.createGroup(Arrays.asList(23L, 29L),
                new ArrayList<>(Arrays.asList(41L, 53L, 67L, 73L, 83L, 91L, 97L)), adminToken);

        verify(spyController, times(1)).getCurrentUserType(adminToken);
        verify(spyController, times(2)).getUserType(anyLong(), anyString());
        verify(spyController, times(1)).setUserType(23, "SECRETARY", adminToken);
        assertEquals("The research group has been saved successfully!",
                resultString);
    }
}