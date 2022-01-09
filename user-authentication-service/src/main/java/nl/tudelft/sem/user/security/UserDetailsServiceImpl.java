package nl.tudelft.sem.user.security;

import java.util.Optional;
import java.util.Set;
import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.response.LoginResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * The type User details service.
 */
@Component
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class UserDetailsServiceImpl implements UserDetailsService {

    private transient UserRepository userRepository;
    private transient JwtUtil jwtUtil;
    private transient AuthenticationManager authenticationManager;

    /**
     * Instantiates a new User details service.
     *
     * @param userRepository        the user repository
     * @param jwtUtil               the jwt util
     * @param authenticationManager the authentication manager
     */
    public UserDetailsServiceImpl(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Create jwt token login response.
     *
     * @param request the request
     * @return the login response
     * @throws Exception the exception
     */
    public LoginResponse createJwtToken(LoginRequest request) throws Exception {
        String userName = request.getNetId();
        String password = request.getPassword();

        this.authenticate(userName, password);
        final UserDetails userDetails = loadUserByUsername(userName);

        return new LoginResponse(jwtUtil.generateToken(userDetails));
    }

    /**
     * Gets authenticated user.
     *
     * @param token the token
     * @return the authenticated user
     */
    public Optional<User> getAuthenticatedUser(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Optional.empty();
        } else {
            String userName = jwtUtil.extractUsername(token.substring(7));

            return userRepository.findByNetId(userName);
        }
    }


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByNetId(username);

        if (user.isPresent()) {
            User userPresent = user.get();

            return new org.springframework.security.core.userdetails.User(
                    userPresent.getNetId(),
                    userPresent.getHashedPassword(),
                    Set.of(new SimpleGrantedAuthority("ROLE_" + userPresent.getType()))
            );
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, userPassword)
            );
        } catch (Exception e) {
            throw new Exception("");
        }
    }
}
