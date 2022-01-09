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
    void testEqualsSameId() {
        User user1 = new User();
        User user2 = new User();

        user1.setId(1L);
        user2.setId(1L);

        Assertions.assertTrue(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentId() {
        User user1 = new User();
        User user2 = new User();

        user1.setId(1L);
        user2.setId(2L);

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsSameName() {
        User user1 = new User();
        User user2 = new User();

        user1.setName("Name");
        user2.setName("Name");

        Assertions.assertTrue(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentName() {
        User user1 = new User();
        User user2 = new User();

        user1.setName("Name");
        user2.setName("Other");

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsSameHashedPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashed = passwordEncoder.encode("test");

        User user1 = new User();
        User user2 = new User();

        user1.setHashedPassword(hashed);
        user2.setHashedPassword(hashed);

        Assertions.assertTrue(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentHashedPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user1 = new User();
        User user2 = new User();

        user1.setHashedPassword(passwordEncoder.encode("test"));
        user2.setHashedPassword(passwordEncoder.encode("other"));

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentValues() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode("password");

        User user1 = new User("random@random.com", "Random name", hashedPassword, Type.EMPLOYEE);
        User user2 = new User("random@email.com", "Other name", hashedPassword, Type.ADMIN);

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsNull() {
        User user1 = new User("user@random.com", "User name", "password123", Type.EMPLOYEE);
        User user2 = null;

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testEqualsDifferentClass() {
        User user1 = new User("user@random.com", "User name", "password123", Type.EMPLOYEE);
        String user2 = "";

        Assertions.assertFalse(user1.equals(user2));
    }

    @Test
    void testHashCode() {
        User user1 = new User();
        User user2 = new User();

        Assertions.assertTrue(user1.hashCode() == user2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User("user@random.com", "User name", "password123", Type.EMPLOYEE);

        Assertions.assertNotNull(user.toString());
    }

}