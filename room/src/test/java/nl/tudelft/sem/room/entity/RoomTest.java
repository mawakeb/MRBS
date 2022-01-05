package nl.tudelft.sem.room.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    transient Room room;

    @BeforeEach
    void setUp() {
        room = new Room(1L, "room", 2L, 200);
    }

    @Test
    void testGetSetId() {
        room.setId(3L);
        assertEquals(3L, room.getId());
    }

    @Test
    void testGetSetName() {
        room.setName("test");
        assertEquals("test", room.getName());
    }

    @Test
    void testGetSetBuildingId() {
        room.setBuildingId(3L);
        assertEquals(3L, room.getBuildingId());
    }

    @Test
    void testGetSetCapacity() {
        room.setCapacity(300);
        assertEquals(300, room.getCapacity());
    }

    @Test
    void testGetSetUnderMaintenance() {
        room.setUnderMaintenance(true);
        assertTrue(room.isUnderMaintenance());
    }

    @Test
    void testEquals() {
        Room room2 = new Room(1L, "room", 2L, 200);
        assertTrue(room.equals(room2));
    }

    @Test
    void testNotEquals() {
        Room room2 = new Room(5L, "room", 2L, 200);
        assertFalse(room.equals(room2));
    }

    @Test
    void testHashCode() {
        int hashed = Objects.hash(1L);
        assertEquals(hashed, room.hashCode());
    }
}