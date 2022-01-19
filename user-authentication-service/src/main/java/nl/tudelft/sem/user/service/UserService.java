package nl.tudelft.sem.user.service;

import java.util.Optional;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
public class UserService {

    private transient PasswordEncoder passwordEncoder;
    private transient UserRepository userRepository;

    /**
     * Instantiates a new User service.
     *
     * @param passwordEncoder the password encoder
     * @param userRepository  the user repository
     */
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Create user.
     *
     * @param registerRequest the register request
     * @return the user
     * @throws NetIdAlreadyExistsException the net id already exists exception
     */
    public User create(RegisterRequest registerRequest) throws NetIdAlreadyExistsException {
        if (userRepository.existsByNetId(registerRequest.getNetId())) {
            throw new NetIdAlreadyExistsException(registerRequest.getNetId());
        } else {
            User newUser = new User(
                    registerRequest.getNetId(),
                    registerRequest.getName(),
                    passwordEncoder.encode(registerRequest.getPassword()),
                    registerRequest.getType()
            );

            return userRepository.save(newUser);
        }
    }

    /**
     * Gets user type.
     *
     * @param userId the user id
     * @return the user type
     */
    public Type getUserType(Long userId) {
        return userRepository.findById(userId).get().getType();
    }

    /**
     * Sets user type.
     *
     * @param userId  the user id
     * @param newType the new type
     * @return the user type
     */
    public String setUserType(Long userId, Type newType) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setType(newType);

            userRepository.save(user);
            return "User type changed successfully";
        } else {
            return "User not found";
        }
    }
}
