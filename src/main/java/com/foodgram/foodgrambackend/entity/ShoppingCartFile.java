package com.foodgram.foodgrambackend.entity;

import lombok.Data;
import org.springframework.core.io.Resource;

public class ShoppingCartFile {
    private final org.springframework.core.io.Resource resource;
    private final String fileName;
    private final String contentType;

    public ShoppingCartFile(org.springframework.core.io.Resource resource, String fileName, String contentType) {
        this.resource = resource;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public Resource getResource() {
        return resource;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }
}
