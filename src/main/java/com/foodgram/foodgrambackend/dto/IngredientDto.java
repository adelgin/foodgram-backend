package com.foodgram.foodgrambackend.dto;

import lombok.Getter;
import lombok.Setter;

public class IngredientDto {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String measurement_unit;

    public IngredientDto(Long id, String name, String measurement_unit) {
        this.id = id;
        this.name = name;
        this.measurement_unit = measurement_unit;
    }
}
