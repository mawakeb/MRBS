package nl.tudelft.sem.group.repository;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Group Repository Interface.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Saves a research group.
     *
     * @param group the research group
     * @return the research group
     */
    Group save(Group group);

    /**
     * Finds a research group by id.
     *
     * @param id the research group id
     * @return an optional containing the found research group
     */
    Optional<Group> findById(Long id);

    /**
     * Find all the research groups.
     *
     * @return a list of all the research groups
     */
    List<Group> findAll();

    /**
     * Find all the research groups within a list of ids.
     *
     * @param ids a list of research group ids
     * @return a list of all the research groups found
     */
    List<Group> findAllById(Iterable<Long> ids);
}
