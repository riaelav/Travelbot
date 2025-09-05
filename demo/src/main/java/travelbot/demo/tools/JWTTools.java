package travelbot.demo.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;


    public String createToken(User user) {

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Expiration Date
                .subject(String.valueOf(user.getId())) // Subject
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token
                .compact();
    }

    public void verifyToken(String accessToken) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(accessToken);
        } catch (Exception ex) {

            throw new UnauthorizedException("Problems with your token"); // --> 401
        }
    }

    public String extractIdFromToken(String accessToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(accessToken).getPayload().getSubject();

    }
}

