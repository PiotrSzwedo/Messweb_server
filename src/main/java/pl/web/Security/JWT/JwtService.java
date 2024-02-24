package pl.web.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import pl.web.Entity.User;
import pl.web.Model.IdModel;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static String KEY = "DVNcVqbN3M2/QnfrW8oXEJZPfMEr0EoeALgoExtkr76pLjphYCxdBT1iR1m6wyLcwsCsosdPvkph/XNt4O2UpQ==\n";

    //Function that extract user id from token
    public String extractUserId(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Function that extract claims from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    //Function that extract all claims from token
    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    //Function that generate new token without map<String, Object> argument
    public String generateToken(IdModel idModel){
        return generateToken(new HashMap<>(), idModel);
    }

    //Function that generate new token
    public String generateToken(Map<String, Object> extractClaoms, IdModel idModel){
        return Jwts.builder()
                .setClaims(extractClaoms)
                .setSubject(String.valueOf(idModel.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2592000000L))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean tokenIsValid(String token, String id){
        final String userId = extractUserId(token);
        return userId.equals(id) && isTokenActive(token);
    }

    private boolean isTokenActive(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(key);
    }
}
