package com.example.project.rest.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.project.domain.entities.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret_password}")
    private String secret;

    @Value("${accessToken_expiration}")
    private int expiresA;

    @Value("${refreshToken_expiration}")
    private int expiresR;

    public String generateAccessToken(Users user){
        return JWT.create().withSubject(user.getEmail())
                .withClaim("id",user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresA))
                .sign(Algorithm.HMAC512(secret));
    }

    public String generateRefreshToken(Users user){
        return  JWT.create().withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresR))
                .sign(Algorithm.HMAC512(secret));
    }

    public String getSubject(String token){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();
    }

    public Date getExpirationDate(String token){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getExpiresAt();
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = getExpirationDate(token);
        return expirationDate.before(new Date());
    }

    public boolean isTokenValid(String token, Users user){
        String username = getSubject(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    public Long getClaimId(HttpServletRequest request){
        var auth = request.getHeader("Authorization");
        var token = auth.replace("Bearer ","");
        return JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token)
                .getClaim("id")
                .asLong();
    }


}
