package nl.tudelft.sem.group.repository;


import nl.tudelft.sem.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The Group Repository Interface.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    /** Saves a research group.
     * @param group the research group
     * @return the research group
     */
    Group save(Group group);

    Optional<Group> findById(Long id);

    List<Group> findAll();

    List<Group> findAllById(Iterable<Long> ids);
}
