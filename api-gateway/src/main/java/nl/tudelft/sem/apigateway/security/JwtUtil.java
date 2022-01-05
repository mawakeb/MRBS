package nl.tudelft.sem.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET = "a25cd3cc-f769-476e-a3bf-c52f069754cd";

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public Boolean isInvalid(String token) {
        return this.extractAllClaims(token).getExpiration().before(new Date());
    }
}
