package com.foodgram.foodgrambackend.dto;

public class RecipeMinifiedDto {
    private Long id;
    private String name;
    private String image;
    private Integer cooking_time;

    // Конструкторы
    public RecipeMinifiedDto() {}

    public RecipeMinifiedDto(Long id, String name, String image, Integer cooking_time) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.cooking_time = cooking_time;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Integer getCooking_time() { return cooking_time; }
    public void setCooking_time(Integer cooking_time) { this.cooking_time = cooking_time; }
}