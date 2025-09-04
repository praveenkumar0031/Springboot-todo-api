package dev.projects.HelloWorld.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component

public class JwtUtil {
    @Value("${SECRET_CODE}")
private String SECRET ;
private final long EXPIRATION_TIME =1000*120;
//1000ms=1sec
private  Key SecrectKey ;
//= Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    @PostConstruct
    public void init() {
        if (SECRET == null || SECRET.length() < 32) {
            throw new IllegalArgumentException(
                    "SECRET_CODE must be set and at least 32 characters long for HS256"
            );
        }
        this.SecrectKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
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
