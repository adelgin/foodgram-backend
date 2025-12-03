package com.foodgram.foodgrambackend.security;

import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class TokenUtils {

    private final String secret = "foodgram-secret-key-2025";
    /**
     * Время истекания access токена - 24 часа
     * Refresh токена - 7 дней
     */
    private final long expirationMs = 86400000;
    private final long refreshTokenExpiration = 604800000;

    public String generateAccessToken(String email) {
        String tokenData = email + "|" + (System.currentTimeMillis() + expirationMs);
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    public String generateRefreshToken(String email) {
        String tokenData = "refresh|" + email + "|" + (System.currentTimeMillis() + refreshTokenExpiration);
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    public boolean isRefreshToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            return decoded.startsWith("refresh|");
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split("\\|");
            return parts[0];
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split("\\|");
            if (parts.length != 2) return false;

            long expiryTime = Long.parseLong(parts[1]);
            return expiryTime > System.currentTimeMillis();

        } catch (Exception e) {
            return false;
        }
    }
}
