package nl.tudelft.sem.user.communication;

import java.util.List;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    public UserController(UserDetailsServiceImpl userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(userDetailsService.createJwtToken(request));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.create(request);

            RegisterResponse responseBody = new RegisterResponse(newUser.getNetId(), newUser.getName(), newUser.getType());

            return ResponseEntity.ok(responseBody);
        } catch (NetIdAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getCurrentUserType")
    public String getCurrentUserType(@RequestHeader("Authorization") String token)
    {
        Optional<User> authenticatedUser = userDetailsService.getAuthenticatedUser(token);

        return authenticatedUser.map(user -> user.getType().toString()).orElse(null);
    }

    @GetMapping("/getCurrentUserID")
    public Long getCurrentUserID(@RequestHeader("Authorization") String token)
    {
        Optional<User> authenticatedUser = userDetailsService.getAuthenticatedUser(token);

        return authenticatedUser.map(User::getId).orElse((long) - 1);
    }

    @GetMapping("/getTeamMemberIDs")
    public List getBuildingOpeningHours(@RequestParam(value = "secretaryUserID") Long secretaryUserID) {
        ArrayList<Long> result = new ArrayList<Long>();
        result.add((long) 1234);
        return result;
    }
}
