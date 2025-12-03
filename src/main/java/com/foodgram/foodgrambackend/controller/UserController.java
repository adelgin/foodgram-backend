package com.foodgram.foodgrambackend.controller;

import com.foodgram.foodgrambackend.dto.*;
import com.foodgram.foodgrambackend.entity.Recipe;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.exception.InvalidPasswordException;
import com.foodgram.foodgrambackend.service.SubscriptionService;
import com.foodgram.foodgrambackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(@RequestParam int page, @RequestParam int limit) {
        var recipes = userService.getAll(page, limit);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getCurrentUserDto(currentUser));
    }

    @PutMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal User currentUser) {
        var response = userService.changeAvatar(file, currentUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/avatar")
    public ResponseEntity<?> deleteAvatar(@AuthenticationPrincipal User currentUser) {
        return userService.deleteAvatar(currentUser) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/me/avatar")
    public ResponseEntity<?> getAvatar(@AuthenticationPrincipal User currentUser) {
        var response = userService.getAvatar(currentUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal User currentUser) {
        try {
            userService.changePassword(changePasswordDto, currentUser);
        } catch (InvalidPasswordException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<PagedResponseDto<UserWithRecipesDto>> getSubscriptions(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Integer recipes_limit) {

        PagedResponseDto<UserWithRecipesDto> response =
                subscriptionService.getSubscriptions(page, limit, currentUser, recipes_limit);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Long id, @AuthenticationPrincipal User currentUser, @RequestParam(required = false) Integer recipes_limit) {
        try {
            UserWithRecipesDto subscription = subscriptionService.subscribe(currentUser.getId(), id, recipes_limit);
            return ResponseEntity.status(201).body(subscription);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        try {
            subscriptionService.unsubscribe(currentUser.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
