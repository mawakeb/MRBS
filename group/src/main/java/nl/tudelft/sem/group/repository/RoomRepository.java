package nl.tudelft.sem.group.repository;


import nl.tudelft.sem.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The User Repository Interface.
 */
public interface RoomRepository extends JpaRepository<Group, Long> {
}
