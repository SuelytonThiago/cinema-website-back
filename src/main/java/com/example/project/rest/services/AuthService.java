package com.example.project.rest.services;

import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.UserLoginDto;
import com.example.project.rest.services.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final UsersService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Map<String,String> generateTokens(UserLoginDto dto){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            var user = (Users) authentication.getPrincipal();

            var accessToken = jwtService.generateAccessToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            Map<String,String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return tokens;
        } catch (BadCredentialsException e){
            throw  new CustomException("the email or password is invalid");
        }
    }

    public String attAccessToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        var token = authHeader.replace("Bearer ","");
        var email = jwtService.getSubject(token);
        var user = userService.findByEmail(email);

        if(jwtService.isTokenValid(token,user)){
            return jwtService.generateAccessToken(user);
        }

        throw new CustomException("invalid Token");
    }


}
