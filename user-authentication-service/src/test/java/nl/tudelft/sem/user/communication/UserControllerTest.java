package nl.tudelft.sem.user.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.communication.response.LoginResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.exception.UserNotFoundException;
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
    void testBadLogin() throws Exception {
        LoginRequest request = new LoginRequest("random@random.com", "password");

        Mockito.when(userDetailsService.createJwtToken(Mockito.any()))
                .thenThrow(new UserNotFoundException("test"));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/user/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "random@random.com",
                "Name",
                "password123'",
                Type.EMPLOYEE
        );

        User newUser = new User("random1@random.com", "Name5", "password123", Type.EMPLOYEE);

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
    void testGetCurrentUserId() throws Exception {
        User newUser = new User("random2@random.com", "Name1",
                "password1234", Type.EMPLOYEE);
        newUser.setId(1L);
        Mockito.when(userDetailsService.getAuthenticatedUser(Mockito.any()))
                .thenReturn(java.util.Optional.of(newUser));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/getCurrentUserId")
                        .header("Authorization", "token1"))
                .andExpect(MockMvcResultMatchers.content().string(newUser.getId().toString()));
    }

    @Test
    void testGetCurrentUserType() throws Exception {
        User newUser = new User("random3@random.com", "Name2",
                "password123", Type.EMPLOYEE);
        Mockito.when(userDetailsService.getAuthenticatedUser(Mockito.any()))
                .thenReturn(java.util.Optional.of(newUser));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/getCurrentUserType")
                        .header("Authorization", "token"))
                .andExpect(MockMvcResultMatchers.content().string("EMPLOYEE"));
    }

    @Test
    void testGetUserType() throws Exception {
        Mockito.when(userService.getUserType(Mockito.any()))
                .thenReturn(Type.valueOf("EMPLOYEE"));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/getUserType")
                        .param("id", "1")
                        .header("Authorization", "token"))
                .andExpect(MockMvcResultMatchers.content().string("EMPLOYEE"));
    }

    @Test
    void testSetUserType() throws Exception {
        Mockito.when(userService.setUserType(Mockito.any(), Mockito.any()))
                .thenReturn("User type changed successfully");

        mockMvc
                .perform(MockMvcRequestBuilders.get("/user/setUserType")
                        .param("id", "1")
                        .param("type", "ADMIN"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("User type changed successfully"));
    }

    @Test
    void testComplete() throws Exception {

    }
}