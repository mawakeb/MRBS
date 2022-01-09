package nl.tudelft.sem.user.repository;

import java.util.Optional;
import nl.tudelft.sem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The User Repository Interface.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    boolean existsByNetId(String netId);

    Optional<User> findByNetId(String netId);
}
