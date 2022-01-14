package nl.tudelft.sem.room.repository;

import java.util.List;
import nl.tudelft.sem.room.entity.EquipmentInRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EquipmentRepository extends JpaRepository<EquipmentInRoom, Long> {

    List<EquipmentInRoom> findAll();

    EquipmentInRoom findById(long id);

    @Query(value = "SELECT e.roomId FROM EquipmentInRoom e WHERE e.equipmentName = ?1")
    List<Long> findAllByEquipmentName(String equipmentName);
}
