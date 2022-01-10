package nl.tudelft.sem.user.communication.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginResponseTest {
    transient LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginResponse = new LoginResponse("random.jwt");
    }

    @Test
    void testGetJwt() {
        Assertions.assertEquals(loginResponse.getJwt(), "random.jwt");
    }

    @Test
    void testSetJwt() {
        String newJwt = "new.jwt";

        loginResponse.setJwt(newJwt);

        Assertions.assertEquals(loginResponse.getJwt(), newJwt);
    }
}