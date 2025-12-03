package com.foodgram.foodgrambackend.repository;

import com.foodgram.foodgrambackend.entity.Favorite;
import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.ShoppingCart;
import com.foodgram.foodgrambackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Boolean existsByUserIdAndRecipeId(Long userId, Long RecipeId);
    Boolean existsByUserAndRecipe(User userId, Recipe recipe);

    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
}
