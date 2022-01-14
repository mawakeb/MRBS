package nl.tudelft.sem.room.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
class EquipmentRepositoryTest {

    @Autowired
    private transient TestEntityManager entityManager;

    @Autowired
    private transient EquipmentRepository repo;

    @Test
    void findAll() {
        EquipmentInRoom equipment1 = new EquipmentInRoom(1L, "chair");
        EquipmentInRoom equipment2 = new EquipmentInRoom(2L, "table");
        EquipmentInRoom equipment3 = new EquipmentInRoom(3L, "board");
        entityManager.persist(equipment1);
        entityManager.persist(equipment2);
        entityManager.persist(equipment3);
        entityManager.flush();

        List<EquipmentInRoom> expected = List.of(equipment1, equipment2, equipment3);
        assertEquals(expected, repo.findAll());
    }

    @Test
    void findById() {
        EquipmentInRoom equipment = new EquipmentInRoom(1L, "chair");
        entityManager.persist(equipment);
        entityManager.flush();
        EquipmentInRoom found = repo.findById(equipment.getId()).orElse(null);

        assertEquals(equipment, found);
    }

    @Test
    void findAllByEquipmentName() {
        EquipmentInRoom equipment1 = new EquipmentInRoom(1L, "chair");
        EquipmentInRoom equipment2 = new EquipmentInRoom(2L, "table");
        EquipmentInRoom equipment3 = new EquipmentInRoom(3L, "board");
        entityManager.persist(equipment1);
        entityManager.persist(equipment2);
        entityManager.persist(equipment3);
        entityManager.flush();

        List<Long> expected = List.of(2L);
        assertEquals(expected, repo.findAllByEquipmentName("table"));
    }
}