package com.foodgram.foodgrambackend.controller;

import com.foodgram.foodgrambackend.dto.IngredientDto;
import com.foodgram.foodgrambackend.entity.Ingredient;
import com.foodgram.foodgrambackend.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/ingredients")
@RestController
public class IngredientController {
    @Autowired
    public IngredientService ingredientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientByid(@RequestParam Long id) {
        IngredientDto response;
        try {
            response = ingredientService.findIngredientById(id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getIngredientByid(@RequestBody String name) {
        IngredientDto response;
        try {
            response = ingredientService.findIngredientByName(name);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
