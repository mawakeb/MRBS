package nl.tudelft.sem.user.communication;

import nl.tudelft.sem.user.security.JwtUtil;
import nl.tudelft.sem.user.security.UserDetailsServiceImpl;
import nl.tudelft.sem.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerCompleteTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient UserDetailsServiceImpl userDetailsService;

    @MockBean
    private transient UserService userService;

    @MockBean
    private transient JwtUtil jwtUtil;

    @Test
    void testNotAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/getCurrentUserType"))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @WithMockUser(username = "random@random.com", roles = {"USER"})
    @Test
    void testIncorrectTokenType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/getCurrentUserType"))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @WithMockUser(username = "random@random.com", roles = {"USER"})
    @Test
    void testAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/getCurrentUserType").header("Authorization", "Bearer token").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
