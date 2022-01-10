package nl.tudelft.sem.user.communication.request;


import nl.tudelft.sem.user.object.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterRequestTest {
    transient RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "random@random.com",
                "Name",
                "password",
                Type.EMPLOYEE
        );
    }

    @Test
    void testGetNetId() {
        Assertions.assertEquals(registerRequest.getNetId(), "random@random.com");
    }

    @Test
    void testSetNetId() {
        String newNetId = "newNetId@random.com";

        registerRequest.setNetId(newNetId);

        Assertions.assertEquals(registerRequest.getNetId(), newNetId);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals(registerRequest.getName(), "Name");
    }

    @Test
    void testSetName() {
        String newName = "New Name";

        registerRequest.setName(newName);

        Assertions.assertEquals(registerRequest.getName(), newName);
    }

    @Test
    void testGetPassword() {
        Assertions.assertEquals(registerRequest.getPassword(), "password");
    }

    @Test
    void testSetPassword() {
        String newPassword = "newPassword";

        registerRequest.setPassword(newPassword);

        Assertions.assertEquals(registerRequest.getPassword(), newPassword);
    }

    @Test
    void testGetType() {
        Assertions.assertEquals(registerRequest.getType(), Type.EMPLOYEE);
    }

    @Test
    void testSetType() {
        Type newType = Type.ADMIN;

        registerRequest.setType(newType);

        Assertions.assertEquals(registerRequest.getType(), newType);
    }
}