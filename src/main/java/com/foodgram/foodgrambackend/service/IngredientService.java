package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.dto.IngredientDto;
import com.foodgram.foodgrambackend.entity.Ingredient;
import com.foodgram.foodgrambackend.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {
    @Autowired
    public IngredientRepository ingredientRepository;

    public IngredientDto findIngredientById(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        if (ingredient.isEmpty()) {
            throw new RuntimeException("Ingredient not found");
        }

        return new IngredientDto(ingredient.get().getId(), ingredient.get().getName(), ingredient.get().getMeasurementUnit());
    }

    public IngredientDto findIngredientByName(String name) {
        Optional<Ingredient> ingredient = ingredientRepository.findByName(name);

        if (ingredient.isEmpty()) {
            throw new RuntimeException("Ingredient not found");
        }

        return new IngredientDto(ingredient.get().getId(), ingredient.get().getName(), ingredient.get().getMeasurementUnit());
    }
}
