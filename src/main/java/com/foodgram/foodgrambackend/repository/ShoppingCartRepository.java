package com.foodgram.foodgrambackend.repository;

import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.ShoppingCart;
import com.foodgram.foodgrambackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Boolean existsByUserIdAndRecipeId(Long currentUserId, Long id);
    Boolean existsByUserAndRecipe(User user, Recipe recipe);

    Optional<ShoppingCart> findByUserAndRecipe(User user, Recipe recipe);
}
