package nl.tudelft.sem.user.service;

import java.util.Optional;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {
    private transient UserRepository userRepository = Mockito.mock(UserRepository.class);
    private transient UserService userService =
            new UserService(new BCryptPasswordEncoder(), userRepository);

    // User creation constants.
    private long userId;
    private String netId;
    private String name;
    private String password;

    @BeforeEach
    void setUp() {
        this.userId = 10L;
        this.netId = "random@random.com";
        this.name = "Name";
        this.password = "password";
    }

    @Test
    void testCreateAlreadyExists() {
        Mockito.when(userRepository.existsByNetId(netId)).thenReturn(true);

        Assertions.assertThrows(NetIdAlreadyExistsException.class, () -> {
            userService.create(
                   new RegisterRequest(netId, name, password, Type.EMPLOYEE)
            );
        });
    }

    @Test
    void testCreateValid() throws NetIdAlreadyExistsException {
        Mockito.when(userRepository.existsByNetId(netId)).thenReturn(false);

        User response = new User(netId, name, password, Type.EMPLOYEE);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(response);

        Assertions.assertNotNull(response.getNetId());
        Assertions.assertEquals(response, userService.create(
                new RegisterRequest(netId, name, password, Type.EMPLOYEE))
        );
    }

    @Test
    void testGetUserType() {
        User user = new User(netId, name, password, Type.SECRETARY);
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertEquals(Type.SECRETARY, userService.getUserType(userId));
    }

    @Test
    void testSetUserTypeInvalidId() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertEquals("User not found", userService.setUserType(userId, Type.SECRETARY));
    }

    @Test
    void testSetUserTypeValid() {
        User user = new User(netId, name, password, Type.SECRETARY);
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User updated = new User(netId, name, password, Type.ADMIN);
        updated.setId(userId);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(updated);

        Assertions.assertEquals("User type changed successfully", userService.setUserType(userId, Type.ADMIN));
    }
}