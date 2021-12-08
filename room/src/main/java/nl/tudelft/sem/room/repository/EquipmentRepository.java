package nl.tudelft.sem.room.repository;

import nl.tudelft.sem.room.entity.EquipmentInRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<EquipmentInRoom, Long> {

    List<EquipmentInRoom> findAll();

    EquipmentInRoom findById(long id);

    List<EquipmentInRoom> findAllByEquipmentName(String equipmentName);
}
