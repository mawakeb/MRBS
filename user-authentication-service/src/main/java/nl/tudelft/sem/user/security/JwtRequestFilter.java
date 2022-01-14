package nl.tudelft.sem.user.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The type Jwt request filter.
 */
@Component
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class JwtRequestFilter extends OncePerRequestFilter {

    private transient JwtUtil jwtUtil;
    private transient UserDetailsServiceImpl userDetailService;

    /**
     * Instantiates a new Jwt request filter.
     *
     * @param userDetailService the user detail service
     * @param jwtUtil           the jwt util
     */
    public JwtRequestFilter(UserDetailsServiceImpl userDetailService, JwtUtil jwtUtil) {
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Optional<String> jwt = this.getJwtFromAuthorizationHeader(request.getHeader("Authorization"));
        Optional<String> netId = Optional.empty();

        if (jwt.isPresent()) {
            netId = Optional.of(jwtUtil.extractUsername(jwt.get()));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailService.loadUserByUsername(netId.get());

                if (jwtUtil.validateToken(jwt.get(), userDetails)) {
                    this.setAuthorization(userDetails, request);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * gets an optional of the jwt from the authorization header.
     *
     * @param authorizationHeader the authorization header from the request
     * @return jwt
     */
    private Optional<String> getJwtFromAuthorizationHeader(final String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return Optional.of(authorizationHeader.substring(7));
        } else {
            return Optional.empty();
        }
    }

    /**
     * sets the authorization for the request given user details.
     *
     * @param userDetails the user details
     * @param request the request
     */
    private void setAuthorization(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
        );

        usernamePasswordAuthenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}

