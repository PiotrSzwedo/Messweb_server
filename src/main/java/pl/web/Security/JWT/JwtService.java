package pl.web.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import pl.web.Entity.User;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Get formatted key
    private Key getKey() {
        final String key = "pdtdy3SjgSCiDuFOqEer3VVAcQo0gq03sZYgOSzbOHRZgotkq5Uq0b23slnRhgqmKZpkXHvygBL29fmq6xqAZ4xs+qFXZXLshZ/F6zAjansXWFbT4JlGlVdP5eA2a8BXGmoJLm0L/ZJth2PuxBEdLwI57AxbW1QNOvd6dhOuTh4RTUbvJNMlqfVdyXv3OpsF5A4Q/kS8K/EmMh70d+s9+V5uoY6qLP+AfQItsmU2sdK1lVZsg3LC6m6a1yDVFN3SzDFamuKwuSPKkOkuuZwwqxCdUjFH2enlZgY5LdVHZjhpiY60rXRaEp0xTWziHWjgf8tRRxqBci3hfSA++xRMcDk5ze0MHRT9BQr6F6ZIWDc=";
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Build new token
    public String buildToken(Map<String, Object> claim, User user) {
        return Jwts
                .builder()
                .setClaims(claim)
                .setSubject(String.valueOf(user.getId()))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Check is token valid
    public Boolean TokenIsValid(String token, User user) {
        final String userId = extractUserId(token);
        return userId.equals(String.valueOf(user.getId()));
    }

    // Extracted user id
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Selected claim extracted
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //All claim extracted
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
}
