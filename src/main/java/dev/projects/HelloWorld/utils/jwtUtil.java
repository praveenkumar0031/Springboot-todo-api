package dev.projects.HelloWorld.utils;

import dev.projects.HelloWorld.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class jwtUtil {
private final String SECRET = "sumaonu";
private final long EXPIRATION_TIME =1000*30;
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
}
