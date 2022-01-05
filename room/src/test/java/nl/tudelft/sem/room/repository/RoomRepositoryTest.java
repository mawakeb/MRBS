package nl.tudelft.sem.room.repository;

import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private transient TestEntityManager entityManager;

    @Autowired
    private transient RoomRepository repo;

    @Test
    void findAll() {
        Room room1 = new Room(2L, "meeting room1", 10L, 20);
        Room room2 = new Room(3L, "meeting room2", 10L, 20);
        Room room3 = new Room(4L, "meeting room3", 10L, 20);
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        entityManager.flush();
        List<Room> expected = List.of(room1, room2, room3);

        assertEquals(expected, repo.findAll());
    }

    @Test
    void findById() {
        Room room = new Room(2L, "meeting room", 10L, 20);
        entityManager.persist(room);
        entityManager.flush();
        Room found = repo.findById(room.getId()).orElse(null);

        assertEquals(room, found);
    }

    @Test
    void findAllById() {
        Room room1 = new Room(2L, "meeting room1", 10L, 20);
        Room room2 = new Room(3L, "meeting room2", 10L, 20);
        Room room3 = new Room(4L, "meeting room3", 10L, 20);
        Room room4 = new Room(5L, "meeting room4", 10L, 20);
        Room room5 = new Room(6L, "meeting room5", 10L, 20);
        Room room6 = new Room(7L, "meeting room6", 10L, 20);
        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        entityManager.persist(room4);
        entityManager.persist(room5);
        entityManager.persist(room6);
        entityManager.flush();

        List<Long> ids = List.of(2L, 5L);

        List<Room> expected = List.of(room1, room4);
        assertEquals(expected, repo.findAllById(ids));
    }
}