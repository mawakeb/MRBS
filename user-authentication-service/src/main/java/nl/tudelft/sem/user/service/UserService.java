package nl.tudelft.sem.user.service;

import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
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
}
