package com.foodgram.foodgrambackend.service;

import com.foodgram.foodgrambackend.dto.ChangePasswordDto;
import com.foodgram.foodgrambackend.dto.UserCreateDto;
import com.foodgram.foodgrambackend.dto.UserResponseDto;
import com.foodgram.foodgrambackend.entity.User;
import com.foodgram.foodgrambackend.exception.InvalidPasswordException;
import com.foodgram.foodgrambackend.repository.SubscriptionRepository;
import com.foodgram.foodgrambackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       SubscriptionRepository subscriptionRepository,
                       ImageService imageService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    }

    public Page<User> getAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findAll(pageable);
    }

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        Optional<User> existingUser = userRepository.findByEmail(userCreateDto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User newUser = new User(
                userCreateDto.getEmail(),
                userCreateDto.getUsername(),
                userCreateDto.getFirstName(),
                userCreateDto.getLastName(),
                userCreateDto.getPassword() != null ? passwordEncoder.encode(userCreateDto.getPassword()) : null
        );

        User createdUser = userRepository.save(newUser);

        return new UserResponseDto(
                createdUser.getId(),
                createdUser.getEmail(),
                createdUser.getUsername(),
                createdUser.getFirstName(),
                createdUser.getLastName()
        );
    }

    public UserResponseDto getCurrentUserDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getActualUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public Map<String, String> changeAvatar(MultipartFile file, User currentUser) {
        try {
            String base64Avatar = imageService.convertToBase64(file);
            currentUser.setAvatar(base64Avatar);
            userRepository.save(currentUser);

            return Map.of(
                    "message", "Avatar uploaded successfully",
                    "avatar", base64Avatar
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteAvatar(User currentUser) {
        try {
            currentUser.setAvatar(null);
            userRepository.save(currentUser);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Map<String, String> getAvatar(User currentUser) {
        if (currentUser.getAvatar() == null) {
            throw new RuntimeException("Аватар не установлен");
        }
        return Map.of("avatar", currentUser.getAvatar());
    }

    public void changePassword(ChangePasswordDto changePasswordDto, User currentUser) {
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), currentUser.getPassword())) {
            throw new InvalidPasswordException("Текущий пароль неверен");
        }

        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), currentUser.getPassword())) {
            throw new InvalidPasswordException("Новый пароль должен отличаться от текущего");
        }

        currentUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(currentUser);
    }
}
