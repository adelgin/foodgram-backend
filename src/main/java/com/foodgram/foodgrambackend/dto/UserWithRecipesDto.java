package com.foodgram.foodgrambackend.dto;

import java.util.List;

public class UserWithRecipesDto {
    private String email;
    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private Boolean is_subscribed;
    private List<RecipeMinifiedDto> recipes;
    private Integer recipes_count;
    private String avatar;

    // Конструкторы
    public UserWithRecipesDto() {}

    public UserWithRecipesDto(String email, Long id, String username, String first_name,
                              String last_name, Boolean is_subscribed, List<RecipeMinifiedDto> recipes,
                              Integer recipes_count, String avatar) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_subscribed = is_subscribed;
        this.recipes = recipes;
        this.recipes_count = recipes_count;
        this.avatar = avatar;
    }

    // Геттеры и сеттеры
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public Boolean getIs_subscribed() { return is_subscribed; }
    public void setIs_subscribed(Boolean is_subscribed) { this.is_subscribed = is_subscribed; }

    public List<RecipeMinifiedDto> getRecipes() { return recipes; }
    public void setRecipes(List<RecipeMinifiedDto> recipes) { this.recipes = recipes; }

    public Integer getRecipes_count() { return recipes_count; }
    public void setRecipes_count(Integer recipes_count) { this.recipes_count = recipes_count; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
