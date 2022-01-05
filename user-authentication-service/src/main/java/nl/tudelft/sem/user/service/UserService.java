package nl.tudelft.sem.user.service;

import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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
