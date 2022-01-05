package nl.tudelft.sem.user.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.request.RegisterRequest;
import nl.tudelft.sem.user.communication.response.RegisterResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.exception.NetIdAlreadyExistsException;
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

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

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
     * Gets current user type.
     *
     * @param token the token
     * @return the current user type
     */
    @GetMapping("/getCurrentUserType")
    public String getCurrentUserType(@RequestHeader("Authorization") String token) {
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

    /**
     * Gets building opening hours.
     *
     * @param secretaryUserId the secretary user id
     * @return the building opening hours
     */
    @GetMapping("/getTeamMemberIDs")
    public List<Long> getBuildingOpeningHours(
            @RequestParam(value = "secretaryUserId") Long secretaryUserId) {
        ArrayList<Long> result = new ArrayList<>();
        result.add((long) 1234);
        return result;
    }
}
