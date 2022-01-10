package nl.tudelft.sem.room.repository;

import java.util.List;
import nl.tudelft.sem.room.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BuildingRepository extends JpaRepository<Building, Long> {

    List<Building> findAll();

    Building findById(long id);
}
