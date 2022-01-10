package nl.tudelft.sem.user.communication.response;

import nl.tudelft.sem.user.object.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterResponseTest {
    transient RegisterResponse registerResponse;

    @BeforeEach
    void setUp() {
        registerResponse = new RegisterResponse("random@random.com", "Name", Type.EMPLOYEE);
    }

    @Test
    void testGetNetId() {
        Assertions.assertEquals(registerResponse.getNetId(), "random@random.com");
    }

    @Test
    void testSetNetId() {
        String newNetId = "newEmail@random.com";

        registerResponse.setNetId(newNetId);

        Assertions.assertEquals(registerResponse.getNetId(), newNetId);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals(registerResponse.getName(), "Name");
    }

    @Test
    void testSetName() {
        String newName = "New Name";

        registerResponse.setName(newName);

        Assertions.assertEquals(registerResponse.getName(), newName);
    }

    @Test
    void testGetType() {
        Assertions.assertEquals(registerResponse.getType(), Type.EMPLOYEE);
    }

    @Test
    void testSetType() {
        registerResponse.setType(Type.ADMIN);

        Assertions.assertEquals(registerResponse.getType(), Type.ADMIN);
    }
}