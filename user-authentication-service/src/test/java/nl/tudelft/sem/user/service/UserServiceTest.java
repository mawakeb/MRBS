package nl.tudelft.sem.user.service;

import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {
    private transient UserRepository userRepository = Mockito.mock(UserRepository.class);
    private transient UserService userService = new UserService(new BCryptPasswordEncoder(), userRepository);

    @Test
    void testCreateAlreadyExists() {
        Mockito.when(userRepository.existsByNetId("random@random.com")).thenReturn(true);

        Assertions.assertThrows(NetIdAlreadyExistsException.class, () -> {
           userService.create(
                   new RegisterRequest("random@random.com", "Name", "password", Type.EMPLOYEE)
           );
        });
    }

    @Test
    void testCreateValid() throws NetIdAlreadyExistsException {
        Mockito.when(userRepository.existsByNetId("other@email.com")).thenReturn(false);

        User response = new User("other@email.com", "Name", "password", Type.EMPLOYEE);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(response);

        Assertions.assertNotNull(response.getNetId());
        Assertions.assertEquals(response, userService.create(
                new RegisterRequest("other@email.com", "Name", "password", Type.EMPLOYEE))
        );
    }
}