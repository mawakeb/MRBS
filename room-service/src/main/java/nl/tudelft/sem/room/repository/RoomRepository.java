package nl.tudelft.sem.room.repository;

import java.util.List;
import nl.tudelft.sem.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The Room Repository Interface.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAll();

    Room findById(long id);

    List<Room> findAllById(Iterable<Long> ids);
}

