package com.example.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

        return http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(builder.pattern("/api/users/create")).permitAll()
                .requestMatchers(builder.pattern("/api/users/update")).hasAnyRole("USER","ADMIN")

                .requestMatchers(builder.pattern("/api/categories/add")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/categories")).permitAll()

                .requestMatchers(builder.pattern("/api/chairs/**")).permitAll()

                .requestMatchers(builder.pattern("/api/movies/add")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/movies/add/category")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/movies/category/**")).permitAll()
                .requestMatchers(builder.pattern("/api/movies/search")).permitAll()
                .requestMatchers(builder.pattern("/api/movies/update/**")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/movies/delete/**")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/movies/**")).permitAll()

                .requestMatchers(builder.pattern("/api/reviews/add")).hasAnyRole("USER","ADMIN")
                .requestMatchers(builder.pattern("/api/reviews/data/**")).hasAnyRole("USER","ADMIN")
                .requestMatchers(builder.pattern("/api/reviews/update/**")).hasAnyRole("USER","ADMIN")
                .requestMatchers(builder.pattern("/api/reviews/delete/**")).hasAnyRole("USER","ADMIN")

                .requestMatchers(builder.pattern("/api/sessions/add")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/sessions/info/**")).permitAll()
                .requestMatchers(builder.pattern("/api/sessions/update")).hasRole("ADMIN")
                .requestMatchers(builder.pattern("/api/sessions")).permitAll() 

                .requestMatchers(builder.pattern("/api/auth/login")).permitAll()
                .requestMatchers(builder.pattern("/api/auth/refresh")).hasAnyRole("USER","ADMIN")

                .requestMatchers(builder.pattern("/api/tickets/purchase")).hasAnyRole("USER","ADMIN")
                .requestMatchers(builder.pattern("/api/tickets")).hasAnyRole("USER","ADMIN")
                .requestMatchers(builder.pattern("/api/tickets/invalidate/**")).hasRole("ADMIN")
                .anyRequest().authenticated().and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }
}
