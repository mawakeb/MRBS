package nl.tudelft.sem.room.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentInRoomTest {

    transient EquipmentInRoom equipment;

    @BeforeEach
    void setUp() {
        equipment = new EquipmentInRoom(1L, "test");
    }

    @Test
    void testGetSetId() {
        equipment.setId(3L);
        assertEquals(3L, equipment.getId());
    }

    @Test
    void testGetSetRoomId() {
        equipment.setRoomId(2L);
        assertEquals(2L, equipment.getRoomId());
    }

    @Test
    void testGetSetEquipmentName() {
        equipment.setEquipmentName("chair");
        assertEquals("chair", equipment.getEquipmentName());
    }
}