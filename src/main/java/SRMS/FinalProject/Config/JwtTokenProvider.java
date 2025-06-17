package SRMS.FinalProject.Config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;


@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;  // This should be injected from application.properties

    @Value("${jwt.expirationTime}")
    private long jwtExpirationTime;  // Ensure this is defined as a long

    private Key key;  // This will hold the secret key for signing and verification

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret is missing in application.properties");
        }
        // Dynamically initialize the key from the injected jwtSecret
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails, Long userId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("userId", userId); // Store userId as a claim

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // Keep email or username here
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))  // Use expiration time from properties
                .signWith(key, SignatureAlgorithm.HS256)  // Use the key here
                .compact();
    }

    /**
     * Extracts the userId from the JWT token's subject.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)  // Use the same key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get("userId").toString()); // Use "userId" from claims
    }

    /**
     * Extracts roles from the JWT token.
     */
    public List<String> getRolesFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove Bearer prefix
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Object rolesObject = claims.get("roles");

            if (rolesObject instanceof List<?>) {
                List<?> rawList = (List<?>) rolesObject;
                return rawList.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }

        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid or expired JWT token while extracting roles", e);
        }
    }

    /**
     * Validates the token format and expiration.
     */
    public boolean validateToken(String token, Long expectedUserId) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long actualUserId = Long.valueOf(claims.getSubject());

            return actualUserId.equals(expectedUserId) && claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts the userId from the token.
     */
    public Long extractUserId(String token) {
        return getUserIdFromToken(token);
    }
}
