package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.dto.*;
import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.RecipeIngredient;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.repository.FavoriteRepository;
import com.foodgram.foodgrambackend.repository.IngredientRepository;
import com.foodgram.foodgrambackend.repository.RecipeRepository;
import com.foodgram.foodgrambackend.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public PagedResponseDto<RecipeResponseDto> getAll(int page, int limit, User currentUser) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Recipe> recipePage = recipeRepository.findAll(pageable);

        List<RecipeResponseDto> recipeDtos = recipePage.getContent().stream()
                .map(recipe -> convertToDto(recipe, currentUser))
                .collect(Collectors.toList());

        String next = recipePage.hasNext() ?
                String.format("/api/recipes/?page=%d&limit=%d", page + 1, limit) : null;

        String previous = recipePage.hasPrevious() ?
                String.format("/api/recipes/?page=%d&limit=%d", page - 1, limit) : null;

        return new PagedResponseDto<>(
                (int) recipePage.getTotalElements(),
                next,
                previous,
                recipeDtos
        );
    }

    private RecipeResponseDto convertToDto(Recipe recipe, User currentUser) {
        List<IngredientInRecipeDto> ingredientDtos = recipe.getRecipeIngredients().stream()
                .map(this::convertIngredientToDto)
                .collect(Collectors.toList());

        Boolean isFavorited = false;
        Boolean isInShoppingCart = false;

        if (currentUser != null) {
            Long currentUserId = currentUser.getId();
            isFavorited = favoriteRepository.existsByUserIdAndRecipeId(currentUserId, recipe.getId());
            isInShoppingCart = shoppingCartRepository.existsByUserIdAndRecipeId(currentUserId, recipe.getId());
        }

        return new RecipeResponseDto(
                recipe.getId(),
                recipe.getAuthor(),
                ingredientDtos,
                isFavorited,
                isInShoppingCart,
                recipe.getName(),
                recipe.getImage(),
                recipe.getText(),
                recipe.getCookingTime()
        );
    }

    private IngredientInRecipeDto convertIngredientToDto(RecipeIngredient recipeIngredient) {
        return new IngredientInRecipeDto(
                recipeIngredient.getIngredient().getId(),
                recipeIngredient.getIngredient().getName(),
                recipeIngredient.getIngredient().getMeasurementUnit(),
                recipeIngredient.getAmount()
        );
    }

    public RecipeResponseDto getById(Long id, User currentUser) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isEmpty()) {
            throw new RuntimeException("Recipe is not found.");
        }

        return convertToDto(recipe.get(), currentUser);
    }

    public RecipeResponseDto create(RecipeCreateDto recipeDto, User author) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setText(recipeDto.getText());
        recipe.setImage(recipeDto.getImage());
        recipe.setCookingTime(recipeDto.getCooking_time());
        recipe.setAuthor(author);

        for (IngredientAmountDto ingredientDto : recipeDto.getIngredients()) {
            var ingredient = ingredientRepository.findById(ingredientDto.getId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientDto.getId()));

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setAmount(ingredientDto.getAmount());

            recipe.getRecipeIngredients().add(recipeIngredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToDto(savedRecipe, author);
    }

    public RecipeResponseDto update(Long id, RecipeCreateDto recipeDto, User currentUser) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only edit your own recipes");
        }

        if (recipeDto.getName() != null) recipe.setName(recipeDto.getName());
        if (recipeDto.getText() != null) recipe.setText(recipeDto.getText());
        if (recipeDto.getImage() != null) recipe.setImage(recipeDto.getImage());
        if (recipeDto.getCooking_time() != null) recipe.setCookingTime(recipeDto.getCooking_time());

        if (recipeDto.getIngredients() != null) {
            recipe.getRecipeIngredients().clear();

            for (IngredientAmountDto ingredientDto : recipeDto.getIngredients()) {
                var ingredient = ingredientRepository.findById(ingredientDto.getId())
                        .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientDto.getId()));

                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setAmount(ingredientDto.getAmount());

                recipe.getRecipeIngredients().add(recipeIngredient);
            }
        }

        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDto(updatedRecipe, currentUser);
    }
    
    public void delete(Long id, User currentUser) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!recipe.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }

        recipeRepository.delete(recipe);
    }
}