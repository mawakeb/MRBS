package nl.tudelft.sem.user.repository;

import nl.tudelft.sem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The User Repository Interface.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNetId(String netId);

    Optional<User> findByNetId(String netId);
}
