package com.foodgram.foodgrambackend.dto;

public class JwtResponse {
    private String authToken;

    public JwtResponse(String token) {
        authToken = token;
    }
}
