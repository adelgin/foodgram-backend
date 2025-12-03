package com.foodgram.foodgrambackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    @Value("${app.avatar.max-size:5242880}")
    private long maxFileSize;

    public String convertToBase64(MultipartFile file) throws IOException {
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size too large. Max: " + maxFileSize + " bytes");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        byte[] fileBytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        return "data:" + contentType + ";base64," + base64;
    }

    public boolean isValidBase64(String base64String) {
        if (base64String == null || !base64String.startsWith("data:image/")) {
            return false;
        }
        try {
            String base64Data = base64String.split(",")[1];
            Base64.getDecoder().decode(base64Data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getImageSize(String base64Avatar) {
        if (base64Avatar == null) return 0;
        try {
            String base64Data = base64Avatar.split(",")[1];
            return base64Data.length() * 3 / 4;
        } catch (Exception e) {
            return 0;
        }
    }
}
