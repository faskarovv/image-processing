package com.example.imageProcessing.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Data
@Service
public class JwtService {

    @Value("${security.jwt.secret.key}")
    private String secretKey;


    @Value("${secuirty.jwt.expiration-time}")
    private long expirationTime;


    public <T> T extractClaims(String token , Function<Claims, T> resolver){
            final Claims claims = extractAllCLaims(token);


            return resolver.apply(claims);
    }

    public Claims extractAllCLaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){


        return Keys.hmacShaKeyFor( Decoders.BASE64.decode(secretKey));
    }

    public String extractUsername(String token){
            return extractClaims(token , Claims::getSubject);
    }


    public String generateToken(UserDetails userDetails){
        HashMap<String , Object> extraClaims = new HashMap<>();

        extraClaims.put("roles" , userDetails.getAuthorities().stream().map(Object::toString).toList());

        return generateToken(extraClaims , userDetails);
    }

    public String generateToken(HashMap<String , Object> extraClaims , UserDetails userDetails){
            return buildToken(extraClaims , userDetails , expirationTime);
    }

    public String buildToken(HashMap<String , Object> extraClaims , UserDetails userDetails , Long expirationTime){

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token , Claims::getExpiration).before(new Date());
    }

}
