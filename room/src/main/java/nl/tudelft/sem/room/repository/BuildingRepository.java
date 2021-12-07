package nl.tudelft.sem.room.repository;


import nl.tudelft.sem.room.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, Long>{

    List<Building> findAll();

    Building findById(long id);
}
