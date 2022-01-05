package nl.tudelft.sem.room.repository;

import nl.tudelft.sem.room.entity.EquipmentInRoom;
import nl.tudelft.sem.room.entity.Room;
import nl.tudelft.sem.room.entity.RoomNotice;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private transient TestEntityManager entityManager;

    @Autowired
    private transient NoticeRepository repo;

    @Test
    void findAll() {
        RoomNotice notice1 = new RoomNotice(1L, 2L, "door is broken");
        RoomNotice notice2 = new RoomNotice(1L, 3L, "problems with computer");
        RoomNotice notice3 = new RoomNotice(1L, 2L, "window is broken");
        entityManager.persist(notice1);
        entityManager.persist(notice2);
        entityManager.persist(notice3);
        entityManager.flush();

       List<RoomNotice> expected = List.of(notice1, notice2, notice3);
        assertEquals(expected, repo.findAll());
    }

    @Test
    void findById() {
        RoomNotice notice = new RoomNotice(1L, 2L, "door is broken");
        entityManager.persist(notice);
        entityManager.flush();
        RoomNotice found = repo.findById(notice.getId()).orElse(null);

        assertEquals(notice, found);
    }

    @Test
    void findByRoomId() {
        RoomNotice notice1 = new RoomNotice(1L, 2L, "door is broken");
        RoomNotice notice2 = new RoomNotice(5L, 1L, "lacks chairs");
        RoomNotice notice3 = new RoomNotice(7L, 2L, "window is broken");
        entityManager.persist(notice1);
        entityManager.persist(notice2);
        entityManager.persist(notice3);
        entityManager.flush();

        List<RoomNotice> expected = List.of(notice1);
        assertEquals(expected, repo.findByRoomId(1L));
    }
}