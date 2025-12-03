package com.foodgram.foodgrambackend.repository;

import com.foodgram.foodgrambackend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthorId(Long authorId);
    List<Recipe> findByNameContainingIgnoreCase(String name);
    Page<Recipe> findByAuthorId(Long authorId, Pageable pageable);

    Integer countByAuthorId(Long id);
}
