package com.foodgram.foodgrambackend.dto;

public class UserResponseDto {
    private String email;
    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String avatar;

    UserResponseDto() {
    }

    public UserResponseDto(Long id, String email, String username, String first_name, String last_name) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public String getLastName() {
        return this.last_name;
    }

    public Long getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }
}
