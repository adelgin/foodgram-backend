package com.foodgram.foodgrambackend.dto;

public class UserCreateDto {
    private String email;
    private String username;
    private String first_name;
    private String last_name;
    private String password;

    UserCreateDto() {
    }

    UserCreateDto(String email, String username, String first_name, String last_name, String password) {
        this.email = email;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
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

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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
