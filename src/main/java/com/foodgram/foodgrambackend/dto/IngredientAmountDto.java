package com.foodgram.foodgrambackend.dto;

public class IngredientAmountDto {
    private Long id;
    private Integer amount;

    public IngredientAmountDto() {}

    public IngredientAmountDto(Long id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}