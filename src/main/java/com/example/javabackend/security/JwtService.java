package com.example.javabackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Краще взяти з application.properties (чи .yml).
    // Тут для прикладу тримаємо жорстко прописаний ключ.
    private static final String SECRET = "YOUR_256_BIT_SECRET_YOUR_256_BIT_SECRET_";

    // 1) Генеруємо токен, використовуючи email (або будь-який username)
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // кладемо роль у claims
        return createToken(claims, email);
    }

    // 2) Створюємо токен із терміном дії (наприклад, 1 година)
    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + 3600000); // 1 година = 3600000мс

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 3) Валідація
    public boolean validateToken(String token, String email) {
        String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    // 4) Витягнути username (email) із токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Чи прострочений токен?
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
