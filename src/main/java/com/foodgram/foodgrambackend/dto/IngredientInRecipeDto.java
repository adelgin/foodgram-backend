package com.foodgram.foodgrambackend.dto;

public class IngredientInRecipeDto {
    private Long id;
    private String name;
    private String measurement_unit;
    private Integer amount;

    public IngredientInRecipeDto() {}

    public IngredientInRecipeDto(Long id, String name, String measurement_unit, Integer amount) {
        this.id = id;
        this.name = name;
        this.measurement_unit = measurement_unit;
        this.amount = amount;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMeasurement_unit() { return measurement_unit; }
    public void setMeasurement_unit(String measurement_unit) { this.measurement_unit = measurement_unit; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}