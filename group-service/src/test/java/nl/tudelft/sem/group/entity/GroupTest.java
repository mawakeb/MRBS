package nl.tudelft.sem.group.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {

    private transient Group group;

    @BeforeEach
    void setUp() {
        List<Long> secretaries = new ArrayList<Long>();
        secretaries.add(1L);
        secretaries.add(23L);
        List<Long> members = new ArrayList<Long>();
        members.add(3L);
        group = new Group(secretaries, members);
    }

    @Test
    void getId() {
        assertEquals(null, group.getId());
    }

    @Test
    void setId() {
        group.setId(0L);
        assertEquals(0L, group.getId());
    }

    @Test
    void getSecretaryIds() {
        assertTrue(group.getSecretaryIds().contains(1L));
        group.getSecretaryIds().remove(1L);
        assertTrue(group.getSecretaryIds().contains(23L));
        group.getSecretaryIds().remove(23L);
        assertTrue(group.getSecretaryIds().isEmpty());
    }

    @Test
    void setSecretaryIds() {
        List<Long> newSecretaryIds = new ArrayList<Long>();
        newSecretaryIds.add(2L);
        group.setSecretaryIds(newSecretaryIds);
        assertTrue(group.getSecretaryIds().contains(2L));
        group.getSecretaryIds().remove(2L);
        assertTrue(group.getSecretaryIds().isEmpty());
        assertEquals(newSecretaryIds, group.getSecretaryIds());
    }

    @Test
    void getMembersIds() {
        assertTrue(group.getMembersIds().contains(3L));
        group.getMembersIds().remove(3L);
        assertTrue(group.getMembersIds().contains(1L));
        group.getMembersIds().remove(1L);
        assertTrue(group.getMembersIds().contains(23L));
        group.getMembersIds().remove(23L);
        assertTrue(group.getMembersIds().isEmpty());
    }

    @Test
    void setMembersIds() {
        List<Long> newMembersIds = new ArrayList<Long>();
        newMembersIds.add(52L);
        group.setMembersIds(newMembersIds);
        assertTrue(group.getMembersIds().contains(52L));
        group.getMembersIds().remove(52L);
        assertTrue(group.getMembersIds().isEmpty());
        assertEquals(newMembersIds, group.getMembersIds());
    }

    @Test
    void testEquals() {
        List<Long> newSecretaries = new ArrayList<Long>();
        newSecretaries.add(1L);
        newSecretaries.add(23L);
        List<Long> newMembers = new ArrayList<Long>();
        newMembers.add(3L);
        Group newGroup = new Group(newSecretaries, newMembers);
        assertEquals(newGroup, this.group);
    }

    @Test
    void testEqualsSameObject() {
        Group newGroup = this.group;
        assertEquals(newGroup, this.group);
    }

    @Test
    void testToString() {
        assertEquals("Group{id=null, secretaryIds=[1, 23], membersIds=[3, 1, 23]}",
                group.toString());
    }
}