package dev.projects.HelloWorld.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component

public class JwtUtil {
private final String SECRET = "keep it secret shhh... keep it secret shhh...";
private final long EXPIRATION_TIME =1000*120;
//1000ms=1sec
private final Key SecrectKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
public String generateToken(String email){
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
            .signWith(SecrectKey, SignatureAlgorithm.HS256) //digital seal
            .compact();
}
public String extractEmail(String token){
    return Jwts.parser()
            .setSigningKey(SecrectKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}
public boolean validateJwtToken(String token){
    try{
        extractEmail(token);
        return true;
    }catch(JwtException ex){
        return false;
    }
}
}
