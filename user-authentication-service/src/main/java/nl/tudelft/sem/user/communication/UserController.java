package nl.tudelft.sem.user.communication;

import java.util.Optional;
import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.communication.response.RegisterResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
import nl.tudelft.sem.user.object.Type;
import nl.tudelft.sem.user.repository.UserRepository;
import nl.tudelft.sem.user.security.UserDetailsServiceImpl;
import nl.tudelft.sem.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private transient UserDetailsServiceImpl userDetailsService;
    private transient UserService userService;

    public UserController(UserDetailsServiceImpl userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    /**
     * Login response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userDetailsService.createJwtToken(request));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Register response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.create(request);

            RegisterResponse responseBody = new RegisterResponse(
                    newUser.getNetId(),
                    newUser.getName(),
                    newUser.getType()
            );

            return ResponseEntity.ok(responseBody);
        } catch (NetIdAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Gets a given user type.
     *
     * @param id the id of the user
     * @return the given user type
     */
    @GetMapping("getUserType")
    public String getUserType(@RequestParam Long id) {
        return userService.getUserType(id).toString();
    }

    /**
     * Sets a given user type.
     *
     * @param id the id of the user
     * @param type the type to set the user to
     * @return the status of the request
     */
    @GetMapping("setUserType")
    public String setUserType(@RequestParam Long id, @RequestParam String type) {
        return userService.setUserType(id, Type.valueOf(type));
    }

    /**
     * Gets current user type.
     *
     * @param token the token
     * @return the current user type
     */
    @GetMapping("/getCurrentUserType")
    public String getCurrentUserType(@RequestHeader("Authorization") String token) {
        System.out.println("random");
        Optional<User> authenticatedUser = userDetailsService.getAuthenticatedUser(token);

        return authenticatedUser.map(user -> user.getType().toString()).orElse(null);
    }

    /**
     * Gets current user id.
     *
     * @param token the token
     * @return the current user id
     */
    @GetMapping("/getCurrentUserId")
    public Long getCurrentUserId(@RequestHeader("Authorization") String token) {
        Optional<User> authenticatedUser = userDetailsService.getAuthenticatedUser(token);

        return authenticatedUser.map(User::getId).orElse((long) - 1);
    }
}
