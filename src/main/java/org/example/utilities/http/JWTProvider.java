package org.example.utilities.http;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;


public class JWTProvider {
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String login){
        return Jwts.builder()
                .setSubject(login)
                .signWith(KEY)
                .compact();
    }

    public static String getLoginFromJwtToken(String token) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
    }
}
