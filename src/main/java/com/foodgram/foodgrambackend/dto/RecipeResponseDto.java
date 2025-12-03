package com.foodgram.foodgrambackend.dto;

import com.foodgram.foodgrambackend.entity.User;

import java.util.List;

public class RecipeResponseDto {
    private Long id;
    private User author;
    private List<IngredientInRecipeDto> ingredients;
    private Boolean is_favorited;
    private Boolean is_in_shopping_cart;
    private String name;
    private String image;
    private String text;
    private Integer cooking_time;

    public RecipeResponseDto() {}

    public RecipeResponseDto(Long id, User author, List<IngredientInRecipeDto> ingredients,
                             Boolean is_favorited, Boolean is_in_shopping_cart, String name,
                             String image, String text, Integer cooking_time) {
        this.id = id;
        this.author = author;
        this.ingredients = ingredients;
        this.is_favorited = is_favorited;
        this.is_in_shopping_cart = is_in_shopping_cart;
        this.name = name;
        this.image = image;
        this.text = text;
        this.cooking_time = cooking_time;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public List<IngredientInRecipeDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientInRecipeDto> ingredients) { this.ingredients = ingredients; }

    public Boolean getIs_favorited() { return is_favorited; }
    public void setIs_favorited(Boolean is_favorited) { this.is_favorited = is_favorited; }

    public Boolean getIs_in_shopping_cart() { return is_in_shopping_cart; }
    public void setIs_in_shopping_cart(Boolean is_in_shopping_cart) { this.is_in_shopping_cart = is_in_shopping_cart; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getCooking_time() { return cooking_time; }
    public void setCooking_time(Integer cooking_time) { this.cooking_time = cooking_time; }
}