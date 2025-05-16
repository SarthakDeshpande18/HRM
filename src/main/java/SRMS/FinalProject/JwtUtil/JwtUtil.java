package SRMS.FinalProject.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey SECRET_KEY;

    @PostConstruct
    public void init() {
        // Generate the key from the secret string or use a secure 256-bit key for HS256
        if (secret != null && !secret.isEmpty()) {
            SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());  // Use the secret from properties if available
        } else {
            SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Use a secure key for HS256 if no secret is provided
        }
    }

    // Generate Token
    public String generateToken(String email, Long userId, List<String> roles, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roles);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 20)) // 20 minutes
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract Token from Header
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("JWT token is missing");
    }

    // Extract Claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract All Claims (if needed elsewhere)
    public Claims extractAllClaims(String token) {
        return getClaims(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Extract user ID
    public Long extractUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims = getClaims(token);
        return ((Number) claims.get("userId")).longValue();  // Correctly extracts userId
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // usually the email
    }

    // Validate the JWT token (check expiration and if it's valid)
    public boolean validateToken(String token, Long userId) {
        try {
            Long extractedUserId = extractUserId(token);  // Extract userId from the token

            if (!extractedUserId.equals(userId)) {
                System.out.println("User ID mismatch.");
                return false;  // User ID mismatch means invalid token
            }

            Claims claims = getClaims(token);  // Use the securely initialized SECRET_KEY

            // Check if the token is expired
            return !claims.getExpiration().before(new Date());  // Token is valid if it's not expired

        } catch (JwtException e) {
            System.out.println("Error during token validation: " + e.getMessage());
            return false;  // Return false if token is invalid or expired
        }
    }
}


//    }
//    public String extractUsername(String token) {
//        return getClaims(token).get("Username", String.class);
//    }
//    //  Extract Email
//    public String extractEmail(String token) {
//        return getClaims(token).getSubject();
//    }


//    //  Extract Roles
//    public List<String> extractRoles(String token) {
//        return getClaims(token).get("roles", List.class);
//    }
//
//    //  Validate Token
//    public boolean validateToken(String token, String userEmail) {
//        return userEmail.equals(extractEmail(token)) && !isTokenExpired(token);
//}


