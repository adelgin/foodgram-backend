package com.foodgram.foodgrambackend.dto;

import java.util.List;

public class RecipeCreateDto {
    private List<IngredientAmountDto> ingredients;
    private String image;
    private String name;
    private String text;
    private Integer cooking_time;

    public RecipeCreateDto() {}

    public List<IngredientAmountDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientAmountDto> ingredients) { this.ingredients = ingredients; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Integer getCooking_time() { return cooking_time; }
    public void setCooking_time(Integer cooking_time) { this.cooking_time = cooking_time; }
}
