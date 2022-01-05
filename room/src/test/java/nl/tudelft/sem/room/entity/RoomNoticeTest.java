package nl.tudelft.sem.room.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomNoticeTest {

    transient Long roomId;
    transient Long reservationId;
    transient String message;
    transient RoomNotice notice;

    @BeforeEach
    void setUp() {
       roomId = 1L;
       reservationId = 2L;
       message = "test";
       notice = new RoomNotice(roomId, reservationId, message);
    }

    @Test
    void setId() {
        long newId = 3L;
        notice.setId(newId);
        assertEquals(newId, notice.getId());
    }

    @Test
    void setRoomId() {
        long newId = 4L;
        notice.setRoomId(newId);
        assertEquals(newId, notice.getRoomId());
    }


    @Test
    void testGetSetReservationId() {
        long newId = 5L;
        notice.setReservationId(newId);
        assertEquals(newId, notice.getReservationId());
    }

    @Test
    void testGetSetMessage() {
        String test2 = "new message";
        notice.setMessage(test2);
        assertEquals(test2, notice.getMessage());
    }
}