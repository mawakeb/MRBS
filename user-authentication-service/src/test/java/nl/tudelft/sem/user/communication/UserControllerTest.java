package nl.tudelft.sem.user.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.communication.response.LoginResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.security.UserDetailsServiceImpl;
import nl.tudelft.sem.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient UserDetailsServiceImpl userDetailsService;

    @MockBean
    private transient UserService userService;

    private transient ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest("random@random.com", "password");

        Mockito.when(userDetailsService.createJwtToken(request))
                .thenReturn(new LoginResponse("random.jwt"));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "random@random.com",
                "Name",
                "password123'",
                Type.EMPLOYEE
        );

        User newUser = new User("random@random.com", "Name", "password123", Type.EMPLOYEE);

        Mockito.when(userService.create(Mockito.any())).thenReturn(newUser);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user/register")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void testRegisterWithException() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "random@email.com",
                "Name",
                "password",
                Type.EMPLOYEE
        );

        Mockito.when(userService.create(Mockito.any()))
                .thenThrow(new NetIdAlreadyExistsException("random@email.com"));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testComplete() throws Exception {

    }
}