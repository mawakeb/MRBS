package nl.tudelft.sem.room.repository;

import nl.tudelft.sem.room.entity.Building;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class BuildingRepositoryTest {

    @Autowired
    private transient TestEntityManager entityManager;

    @Autowired
    private transient BuildingRepository repo;

    @Test
    void findAll() {
        Building building1 = new Building(1L, "test1", LocalTime.of(8,0), LocalTime.of(18,0) );
        Building building2 = new Building(2L, "test2", LocalTime.of(8,0), LocalTime.of(18,0) );
        Building building3 = new Building(3L, "test3", LocalTime.of(8,0), LocalTime.of(18,0) );
        entityManager.persist(building1);
        entityManager.persist(building2);
        entityManager.persist(building3);
        entityManager.flush();

        List<Building> expected = List.of(building1, building2, building3);
        assertEquals(expected, repo.findAll());
    }

    @Test
    void findById() {
        // Sets up a dummy DB
        Building building = new Building(1L, "test", LocalTime.of(8,0), LocalTime.of(18,0) );
        entityManager.persist(building);
        entityManager.flush();
        Building found = repo.findById(1L);

        assertEquals(building, found);
    }
}