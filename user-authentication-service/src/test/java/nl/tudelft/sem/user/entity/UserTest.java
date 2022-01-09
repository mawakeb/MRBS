package nl.tudelft.sem.user.entity;

import nl.tudelft.sem.user.object.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserTest {

    @Test
    void testGetAndSetId() {
        User user = new User();
        user.setId(1L);

        Assertions.assertEquals(user.getId(), 1L);
    }

    @Test
    void testGetAndSetType() {
        User user = new User();
        user.setType(Type.EMPLOYEE);

        Assertions.assertEquals(user.getType(), Type.EMPLOYEE);
    }

    @Test
    void testGetAndSetNetId() {
        User user = new User();

        String netId = "random@random.com";

        user.setNetId(netId);

        Assertions.assertEquals(user.getNetId(), netId);
    }

    @Test
    void testGetAndSetName() {
        User user = new User();

        String name = "Random name";

        user.setName(name);

        Assertions.assertEquals(user.getName(), name);
    }

    @Test
    void testGetAndSetHashedPassword() {
        User user = new User();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode("password");

        user.setHashedPassword(hashedPassword);

        Assertions.assertEquals(user.getHashedPassword(), hashedPassword);
    }

    @Test
    void testEqualsSameMemoryAddress() {
        User user1 = new User();
        User user2 = user1;

        Assertions.assertTrue(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentMemoryAddress() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode("password");

        User user1 = new User("random@email.com", "Random random", hashedPassword, Type.EMPLOYEE);
        User user2 = new User("random@email.com", "Random random", hashedPassword, Type.EMPLOYEE);

        Assertions.assertTrue(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentValues() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode("password");

        User user1 = new User("random@random.com", "Random name", hashedPassword, Type.EMPLOYEE);
        User user2 = new User("random@email.com", "Other name", hashedPassword, Type.ADMIN);

        Assertions.assertFalse(user1.equals(user2));
    }

}