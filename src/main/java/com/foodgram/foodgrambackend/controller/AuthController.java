package com.foodgram.foodgrambackend.controller;

import com.foodgram.foodgrambackend.dto.JwtResponse;
import com.foodgram.foodgrambackend.dto.LoginRequest;
import com.foodgram.foodgrambackend.dto.RefreshRequest;
import com.foodgram.foodgrambackend.dto.UserCreateDto;
import com.foodgram.foodgrambackend.security.TokenUtils;
import com.foodgram.foodgrambackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/token/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            String token = tokenUtils.generateAccessToken(loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!tokenUtils.validateToken(refreshToken) || !tokenUtils.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = tokenUtils.getEmailFromToken(refreshToken);
        String newAccessToken = tokenUtils.generateAccessToken(email);

        return ResponseEntity.ok(new JwtResponse(newAccessToken));
    }

    @PostMapping("/token/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        var response = userService.createUser(userCreateDto);

        return ResponseEntity.ok(response);
    }
}
