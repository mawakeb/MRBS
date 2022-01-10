package nl.tudelft.sem.user.communication.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    transient LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("random@random.com", "password");
    }

    @Test
    void testGetNetId() {
        Assertions.assertEquals(loginRequest.getNetId(), "random@random.com");
    }

    @Test
    void testSetNetId() {
        String newNetId = "newNetId@random.com";

        loginRequest.setNetId(newNetId);

        Assertions.assertEquals(loginRequest.getNetId(), newNetId);
    }

    @Test
    void testGetPassword() {
        Assertions.assertEquals(loginRequest.getPassword(), "password");
    }

    @Test
    void testSetPassword() {
        String newPassword = "newPassword";

        loginRequest.setPassword(newPassword);

        Assertions.assertEquals(loginRequest.getPassword(), newPassword);
    }
}