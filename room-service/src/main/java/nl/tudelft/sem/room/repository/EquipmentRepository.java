package nl.tudelft.sem.room.repository;

import java.util.List;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipmentRepository extends JpaRepository<EquipmentInRoom, Long> {

    List<EquipmentInRoom> findAll();

    EquipmentInRoom findById(long id);

    List<EquipmentInRoom> findAllByEquipmentName(String equipmentName);
}
