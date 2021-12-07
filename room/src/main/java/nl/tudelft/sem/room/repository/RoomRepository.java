package nl.tudelft.sem.room.repository;


import nl.tudelft.sem.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The User Repository Interface.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
}
