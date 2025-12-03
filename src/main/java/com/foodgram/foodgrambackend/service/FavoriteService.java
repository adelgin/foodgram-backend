package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.entity.Favorite;
import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.ShoppingCart;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.repository.FavoriteRepository;
import com.foodgram.foodgrambackend.repository.IngredientRepository;
import com.foodgram.foodgrambackend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public void addRecipeToFavorite(User user, Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found");
        }
        Recipe recipe = recipeOptional.get();

        boolean alreadyInCart = favoriteRepository.existsByUserAndRecipe(user, recipe);
        if (alreadyInCart) {
            throw new RuntimeException("Recipe already in shopping cart");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        favoriteRepository.save(favorite);
    }

    public void removeRecipeFromFavorite(User user, Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found");
        }
        Recipe recipe = recipeOptional.get();

        Optional<Favorite> favoriteOptional = favoriteRepository.findByUserAndRecipe(user, recipe);
        if (favoriteOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found in shopping cart");
        }

        favoriteRepository.delete(favoriteOptional.get());
    }
}
