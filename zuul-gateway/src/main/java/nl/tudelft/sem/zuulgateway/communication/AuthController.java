package nl.tudelft.sem.zuulgateway.communication;

import nl.tudelft.sem.zuulgateway.dto.request.RequestUserDetails;
import nl.tudelft.sem.zuulgateway.dto.response.ResponseToken;
import nl.tudelft.sem.zuulgateway.service.MyUserDetailsService;
import nl.tudelft.sem.zuulgateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private transient AuthenticationManager authenticationManager;

    @Autowired
    private transient MyUserDetailsService myUserDetailsService;

    @Autowired
    private transient JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody RequestUserDetails requestUserDetails) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUserDetails.getNetId(), requestUserDetails.getPassword())
            );
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(requestUserDetails.getNetId());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ResponseToken(jwt));
    }
}
