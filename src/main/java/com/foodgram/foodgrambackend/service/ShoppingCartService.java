package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.entity.*;
import com.foodgram.foodgrambackend.repository.RecipeRepository;
import com.foodgram.foodgrambackend.repository.ShoppingCartRepository;
import com.foodgram.foodgrambackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartFile generateShoppingList(User user, String acceptHeader) {
        List<Recipe> shoppingCartRecipes = user.getShoppingCartRecipes();

        Map<Ingredient, Integer> aggregatedIngredients = aggregateIngredients(shoppingCartRecipes);

        return generateFile(aggregatedIngredients, acceptHeader);
    }

    private Map<Ingredient, Integer> aggregateIngredients(List<Recipe> recipes) {
        Map<Ingredient, Integer> result = new HashMap<>();

        for (Recipe recipe : recipes) {
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                Ingredient ingredient = recipeIngredient.getIngredient();
                Integer amount = recipeIngredient.getAmount();

                result.merge(ingredient, amount, Integer::sum);
            }
        }
        return result;
    }

    private ShoppingCartFile generateFile(Map<Ingredient, Integer> ingredients, String acceptHeader) {
        if (acceptHeader.contains("pdf")) {
            return generatePdfFile(ingredients);
        } else if (acceptHeader.contains("csv")) {
            return generateCsvFile(ingredients);
        } else {
            return generateTextFile(ingredients);
        }
    }

    private ShoppingCartFile generateTextFile(Map<Ingredient, Integer> ingredients) {
        StringBuilder content = new StringBuilder();

        content.append("Foodgram - Список покупок\n");
        content.append("=".repeat(50)).append("\n\n");

        if (ingredients.isEmpty()) {
            content.append("Ваш список покупок пуст.\n");
            content.append("Добавьте рецепты в корзину, чтобы сформировать список покупок.\n");
        } else {
            Map<Character, List<Map.Entry<Ingredient, Integer>>> groupedByLetter = ingredients.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> entry.getKey().getName()))
                    .collect(Collectors.groupingBy(
                            entry -> entry.getKey().getName().toUpperCase().charAt(0)
                    ));

            for (Character letter : groupedByLetter.keySet().stream().sorted().toList()) {
                content.append(letter).append(":\n");

                for (Map.Entry<Ingredient, Integer> entry : groupedByLetter.get(letter)) {
                    Ingredient ingredient = entry.getKey();
                    content.append(String.format("  • %s - %d %s\n",
                            ingredient.getName(),
                            entry.getValue(),
                            ingredient.getMeasurementUnit()));
                }
                content.append("\n");
            }

            content.append("=".repeat(50)).append("\n");
            content.append(String.format("Итого: %d позиций\n", ingredients.size()));
        }

        content.append(String.format("Дата формирования: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));

        return createFileResource(content.toString(), "shopping_list.txt", "text/plain");
    }

    private ShoppingCartFile generateCsvFile(Map<Ingredient, Integer> ingredients) {
        StringBuilder content = new StringBuilder();
        content.append("Продукт;Количество;Единица измерения\n");

        if (ingredients.isEmpty()) {
            content.append("Список покупок пуст;;\n");
        } else {
            List<Map.Entry<Ingredient, Integer>> sortedIngredients = ingredients.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> entry.getKey().getName()))
                    .toList();

            for (Map.Entry<Ingredient, Integer> entry : sortedIngredients) {
                Ingredient ingredient = entry.getKey();
                content.append(String.format("%s;%d;%s\n",
                        ingredient.getName(),
                        entry.getValue(),
                        ingredient.getMeasurementUnit()));
            }

            content.append(String.format("Итого;%d;позиций\n", ingredients.size()));
        }

        content.append(String.format("Дата формирования;%s;",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));

        return createFileResource(content.toString(), "shopping_list.csv", "text/csv");
    }

    private ShoppingCartFile generatePdfFile(Map<Ingredient, Integer> ingredients) {
        try {
            String textContent = generatePdfTextContent(ingredients);
            return createFileResource(textContent, "shopping_list.pdf", "application/pdf");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации PDF", e);
        }
    }

    private String generatePdfTextContent(Map<Ingredient, Integer> ingredients) {
        StringBuilder content = new StringBuilder();

        content.append("Foodgram - Список покупок\n");
        content.append("=".repeat(50)).append("\n\n");

        if (ingredients.isEmpty()) {
            content.append("Ваш список покупок пуст.\n");
        } else {
            List<Map.Entry<Ingredient, Integer>> sortedIngredients = ingredients.entrySet()
                    .stream()
                    .sorted(Comparator.comparing(entry -> entry.getKey().getName()))
                    .toList();

            for (Map.Entry<Ingredient, Integer> entry : sortedIngredients) {
                Ingredient ingredient = entry.getKey();
                content.append(String.format("• %s - %d %s\n",
                        ingredient.getName(),
                        entry.getValue(),
                        ingredient.getMeasurementUnit()));
            }

            content.append("\n").append("=".repeat(50)).append("\n");
            content.append(String.format("Итого: %d позиций\n", ingredients.size()));
        }

        content.append(String.format("Дата формирования: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));

        return content.toString();
    }

    private ShoppingCartFile createFileResource(String content, String fileName, String contentType) {
        ByteArrayResource resource = new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
        return new ShoppingCartFile(resource, fileName, contentType);
    }

    public int getTotalIngredientsCount(User user) {
        List<Recipe> shoppingCartRecipes = user.getShoppingCartRecipes();
        Map<Ingredient, Integer> aggregatedIngredients = aggregateIngredients(shoppingCartRecipes);
        return aggregatedIngredients.size();
    }

    public int getTotalProductsCount(User user) {
        List<Recipe> shoppingCartRecipes = user.getShoppingCartRecipes();
        Map<Ingredient, Integer> aggregatedIngredients = aggregateIngredients(shoppingCartRecipes);
        return aggregatedIngredients.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void addRecipeToShoppingCart(User user, Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found");
        }
        Recipe recipe = recipeOptional.get();

        boolean alreadyInCart = shoppingCartRepository.existsByUserAndRecipe(user, recipe);
        if (alreadyInCart) {
            throw new RuntimeException("Recipe already in shopping cart");
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setRecipe(recipe);

        shoppingCartRepository.save(shoppingCart);
    }

    public void removeRecipeFromShoppingCart(User user, Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found");
        }
        Recipe recipe = recipeOptional.get();

        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUserAndRecipe(user, recipe);
        if (shoppingCartOptional.isEmpty()) {
            throw new RuntimeException("Recipe not found in shopping cart");
        }

        shoppingCartRepository.delete(shoppingCartOptional.get());
    }
}