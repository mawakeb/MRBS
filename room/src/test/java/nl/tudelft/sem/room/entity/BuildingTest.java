package nl.tudelft.sem.room.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    long id;
    String name;
    LocalTime openTime;
    LocalTime closeTime;
    transient Building building;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "test";
        openTime = LocalTime.of(8,0);
        closeTime = LocalTime.of(18,30);
        building = new Building(id, name, openTime, closeTime);
    }

    @Test
    void getId() {
        assertEquals(id, building.getId());
    }

    @Test
    void setId() {
        long newId = 5L;
        building.setId(newId);
        assertEquals(newId, building.getId());
    }

    @Test
    void getName() {
        assertEquals(name, building.getName());
    }

    @Test
    void setName() {
        String newName = "newName";
        building.setName(newName);
        assertEquals(newName, building.getName());
    }

    @Test
    void getOpenTime() {
        assertEquals(openTime, building.getOpenTime());
    }

    @Test
    void setOpenTime() {
        LocalTime newTime = LocalTime.of(9,0);
        building.setOpenTime(newTime);
        assertEquals(newTime, building.getOpenTime());
    }

    @Test
    void getCloseTime() {
        assertEquals(closeTime, building.getCloseTime() );
    }

    @Test
    void setCloseTime() {
        LocalTime newTime = LocalTime.of(9,0);
        building.setCloseTime(newTime);
        assertEquals(newTime, building.getCloseTime());
    }

    @Test
    void isOpen() {
        LocalTime start = LocalTime.of(9,0);
        LocalTime end = LocalTime.of(10,0);
        assertTrue(building.isOpen(start, end));
    }

    @Test
    void tooEarly() {
        LocalTime start = LocalTime.of(6,0);
        LocalTime end = LocalTime.of(10,0);
        assertFalse(building.isOpen(start, end));
    }

    @Test
    void tooLate() {
        LocalTime start = LocalTime.of(9,0);
        LocalTime end = LocalTime.of(23,0);
        assertFalse(building.isOpen(start, end));
    }

    @Test
    void testEquals() {
        Building fake = new Building(id, name, openTime, closeTime);
        assertTrue(building.equals(fake));
    }

    @Test
    void testNotEquals() {
        Building fake = new Building(3L, name, openTime, closeTime);
        assertFalse(building.equals(fake));
    }

    @Test
    void testHashCode() {
        int hashed = Objects.hash(id);
        assertEquals(hashed, building.hashCode());
    }
}