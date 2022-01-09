package nl.tudelft.sem.user.security;

import java.util.Optional;
import java.util.Set;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceImplTest {
    private transient UserRepository userRepository = Mockito.mock(UserRepository.class);
    private transient JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private transient AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private transient UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository, jwtUtil, authenticationManager);

    @Test
    void testGetAuthenticatedUserNullToken() {
        User user = new User("random@random.com", "Name", "password", Type.EMPLOYEE);

        Mockito.when(jwtUtil.extractUsername(Mockito.any())).thenReturn("random@random.com");
        Mockito.when(userRepository.findByNetId(Mockito.any())).thenReturn(Optional.of(user));

        Optional<User> response = userDetailsService.getAuthenticatedUser(null);

        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void testGetAuthenticatedUserNotStartingWithBearer() {
        User user = new User("user@random.com", "Name", "password", Type.EMPLOYEE);

        Mockito.when(jwtUtil.extractUsername(Mockito.any())).thenReturn("user@random.com");
        Mockito.when(userRepository.findByNetId(Mockito.any())).thenReturn(Optional.of(user));

        Optional<User> response = userDetailsService.getAuthenticatedUser("token");

        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void testGetAuthenticatedUser() {
        User user = new User("random@email.com", "User", "password123", Type.EMPLOYEE);

        Mockito.when(jwtUtil.extractUsername(Mockito.any())).thenReturn("random@email.com");
        Mockito.when(userRepository.findByNetId(Mockito.any())).thenReturn(Optional.of(user));

        Optional<User> response = userDetailsService.getAuthenticatedUser("Bearer token");

        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get(), user);
    }

    @Test
    void testLoadUserByUserNameNotFound() {
        Mockito.when(userRepository.findByNetId(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("random@email.com");
        });
    }

    @Test
    void testLoadUserByUserNameValid() {
        User user = new User("random@user.com", "User", "password123", Type.EMPLOYEE);

        Mockito.when(userRepository.findByNetId(Mockito.any())).thenReturn(Optional.of(user));

        Assertions.assertEquals(
                userDetailsService.loadUserByUsername("random@user.com"),
                new org.springframework.security.core.userdetails.User(
                        "random@user.com",
                        "password123",
                        Set.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                )
        );
    }

}