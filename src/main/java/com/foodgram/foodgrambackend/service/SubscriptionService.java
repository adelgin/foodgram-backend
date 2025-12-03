package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.dto.PagedResponseDto;
import com.foodgram.foodgrambackend.dto.RecipeMinifiedDto;
import com.foodgram.foodgrambackend.dto.UserWithRecipesDto;
import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.Subscription;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.repository.RecipeRepository;
import com.foodgram.foodgrambackend.repository.SubscriptionRepository;
import com.foodgram.foodgrambackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    public PagedResponseDto<UserWithRecipesDto> getSubscriptions(int page, int limit, User currentUser, Integer recipesLimit) {
        Pageable pageable = PageRequest.of(page, limit);

        Page<Subscription> subscriptionPage = subscriptionRepository.findBySubscriberId(currentUser.getId(), pageable);

        if (subscriptionPage.getContent().isEmpty()) {
            return new PagedResponseDto<>(0, null, null, new ArrayList<>());
        }

        List<UserWithRecipesDto> userDtos = subscriptionPage.getContent().stream()
                .map(subscription -> convertSubscriptionToDto(subscription, recipesLimit))
                .collect(Collectors.toList());

        String next = subscriptionPage.hasNext() ?
                String.format("/api/users/subscriptions/?page=%d&limit=%d%s",
                        page + 1, limit,
                        recipesLimit != null ? "&recipes_limit=" + recipesLimit : "") : null;

        String previous = subscriptionPage.hasPrevious() ?
                String.format("/api/users/subscriptions/?page=%d&limit=%d%s",
                        page - 1, limit,
                        recipesLimit != null ? "&recipes_limit=" + recipesLimit : "") : null;

        return new PagedResponseDto<>(
                (int) subscriptionPage.getTotalElements(),
                next,
                previous,
                userDtos
        );
    }

    private UserWithRecipesDto convertSubscriptionToDto(Subscription subscription, Integer recipesLimit) {
        User author = subscription.getAuthor();

        List<Recipe> authorRecipes;
        if (recipesLimit != null && recipesLimit > 0) {
            Pageable recipesPageable = PageRequest.of(0, recipesLimit, Sort.by("createdAt").descending());
            authorRecipes = recipeRepository.findByAuthorId(author.getId(), recipesPageable).getContent();
        } else {
            authorRecipes = recipeRepository.findByAuthorId(author.getId());
        }

        List<RecipeMinifiedDto> recipeDtos = authorRecipes.stream()
                .map(recipe -> new RecipeMinifiedDto(
                        recipe.getId(),
                        recipe.getName(),
                        recipe.getImage(),
                        recipe.getCookingTime()
                ))
                .collect(Collectors.toList());

        Integer recipesCount = recipeRepository.countByAuthorId(author.getId());

        return new UserWithRecipesDto(
                author.getEmail(),
                author.getId(),
                author.getActualUsername(),
                author.getFirstName(),
                author.getLastName(),
                true,
                recipeDtos,
                recipesCount,
                author.getAvatar()
        );
    }

    public UserWithRecipesDto subscribe(Long subscriberId, Long authorId, Integer recipesLimit) {
        if (subscriptionRepository.existsBySubscriberIdAndAuthorId(subscriberId, authorId)) {
            throw new RuntimeException("Already subscribed to this user");
        }

        if (subscriberId.equals(authorId)) {
            throw new RuntimeException("Cannot subscribe to yourself");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with id: " + subscriberId));

        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setAuthor(author);

        subscriptionRepository.save(subscription);

        return convertSubscriptionToDto(subscription, recipesLimit);
    }

    public void unsubscribe(Long subscriberId, Long authorId) {
        Subscription subscription = subscriptionRepository.findBySubscriberIdAndAuthorId(subscriberId, authorId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }
}
