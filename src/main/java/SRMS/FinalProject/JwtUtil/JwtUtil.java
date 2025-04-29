package SRMS.FinalProject.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key SECRET_KEY;

    @PostConstruct
    public void init() {
        // Generate the key from the secret string
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate Token
    public String generateToken(String email, Long userId, List<String> roles,String Username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roles);
        claims.put("Username", Username);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 20)) // 20 minutes
                .signWith(SECRET_KEY)
                .compact();
    }

    //  Extract Token from Header
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT token is missing");
    }

    //  Extract Claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //  Extract All Claims (if needed elsewhere)
    public Claims extractAllClaims(String token) {
        return getClaims(token);
    }

    //  Check if token is expired
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    //  Extract user ID
    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }
    public String extractUsername(String token) {
        return getClaims(token).get("Username", String.class);
    }
    //  Extract Email
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }


    //  Extract Roles
    public List<String> extractRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    //  Validate Token
    public boolean validateToken(String token, String userEmail) {
        return userEmail.equals(extractEmail(token)) && !isTokenExpired(token);
}
}