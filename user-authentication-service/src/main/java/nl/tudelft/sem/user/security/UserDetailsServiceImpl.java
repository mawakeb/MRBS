package nl.tudelft.sem.user.security;

import nl.tudelft.sem.user.communication.request.LoginRequest;
import nl.tudelft.sem.user.communication.response.LoginResponse;
import nl.tudelft.sem.user.entity.User;
import nl.tudelft.sem.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserDetailsServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse createJwtToken(LoginRequest request) throws Exception {
        String userName = request.getNetId();
        String password = request.getPassword();

        this.authenticate(userName, password);
        final UserDetails userDetails = loadUserByUsername(userName);

        return new LoginResponse(jwtUtil.generateToken(userDetails));
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

            return new org.springframework.security.core.userdetails.User(userPresent.getNetId(), userPresent.getHashedPassword(), this.getAuthoritiesForUser(userPresent));
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthoritiesForUser(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        return Set.of(new SimpleGrantedAuthority("ROLE_" + user.getType()));
    }

    private void authenticate(String userName, String userPassword) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        } catch(BadCredentialsException e) {
            throw new Exception("Bad credentials from user");
        }
    }
}
