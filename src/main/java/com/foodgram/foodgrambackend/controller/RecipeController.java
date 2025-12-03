package com.foodgram.foodgrambackend.controller;

import com.foodgram.foodgrambackend.dto.*;
import com.foodgram.foodgrambackend.entity.ShoppingCartFile;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.service.FavoriteService;
import com.foodgram.foodgrambackend.service.RecipeService;
import com.foodgram.foodgrambackend.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/")
    public ResponseEntity<PagedResponseDto<RecipeResponseDto>> getAllRecipes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int limit, @AuthenticationPrincipal User currentUser) {

        PagedResponseDto<RecipeResponseDto> response = recipeService.getAll(page, limit, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        RecipeResponseDto recipe = recipeService.getById(id, currentUser);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeCreateDto recipeDto, @AuthenticationPrincipal User currentUser) {
        RecipeResponseDto createdRecipe = recipeService.create(recipeDto, currentUser);
        return ResponseEntity.status(201).body(createdRecipe);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody RecipeCreateDto recipeDto, @AuthenticationPrincipal User currentUser) {
        RecipeResponseDto updatedRecipe = recipeService.update(id, recipeDto, currentUser);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        recipeService.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download_shopping_cart")
    public ResponseEntity<org.springframework.core.io.Resource> downloadShoppingCart(@AuthenticationPrincipal User user, @RequestHeader(value = "Accept", defaultValue = "text/plain") String acceptHeader) {
        ShoppingCartFile file = shoppingCartService.generateShoppingList(user, acceptHeader);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getResource());
    }

    @PostMapping("/{id}/shopping_cart")
    public ResponseEntity<?> addNewRecipeInShoppingCart(@AuthenticationPrincipal User user, @RequestParam Long id) {
        try {
            shoppingCartService.addRecipeToShoppingCart(user, id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/shopping_cart")
    public ResponseEntity<?> deleteRecipeFromShoppingCart(@AuthenticationPrincipal User user, @RequestParam Long id) {
        try {
            shoppingCartService.removeRecipeFromShoppingCart(user, id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/favorite")
    public ResponseEntity<?> addNewRecipeInFavorite(@AuthenticationPrincipal User user, @RequestParam Long id) {
        try {
            favoriteService.addRecipeToFavorite(user, id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<?> deleteRecipeFromFavorite(@AuthenticationPrincipal User user, @RequestParam Long id) {
        try {
            favoriteService.removeRecipeFromFavorite(user, id);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}