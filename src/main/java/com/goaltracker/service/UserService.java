package com.goaltracker.service;

import com.goaltracker.dto.UserDto;
import com.goaltracker.entity.User;
import com.goaltracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserService - Business logic layer for user management
 * This service handles user registration, authentication, and data validation.
 * It includes password encryption and user data conversion between entities and DTOs.
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * Registers a new user in the system
     * Validates user data, checks for duplicates, and encrypts password
     * 
     * @param userDto The user data from registration form
     * @return The created user DTO without password
     * @throws RuntimeException if validation fails or user already exists
     */
    public UserDto registerUser(UserDto userDto) {
        // Validate password confirmation
        if (!userDto.isPasswordMatching()) {
            throw new RuntimeException("Password and confirm password do not match");
        }
        
        // Check if username already exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Convert to DTO and return (without password)
        return convertToDto(savedUser);
    }
    
    /**
     * Authenticates a user with username and password
     * Used for login functionality
     * 
     * @param username The username to authenticate
     * @param password The plain text password
     * @return Optional containing user DTO if authentication succeeds
     */
    public Optional<UserDto> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(convertToDto(user));
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds a user by their ID
     * Returns user data without sensitive information
     * 
     * @param userId The ID of the user to find
     * @return Optional containing user DTO if found
     */
    public Optional<UserDto> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto);
    }
    
    /**
     * Finds a user by their username
     * Returns user data without sensitive information
     * 
     * @param username The username to search for
     * @return Optional containing user DTO if found
     */
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }
    
    /**
     * Finds a user entity by ID
     * Used internally when we need the full entity with relationships
     * 
     * @param userId The ID of the user to find
     * @return Optional containing user entity if found
     */
    public Optional<User> getUserEntityById(Long userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Finds a user entity by username
     * Used internally when we need the full entity with relationships
     * 
     * @param username The username to search for
     * @return Optional containing user entity if found
     */
    public Optional<User> getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Checks if a username is available for registration
     * 
     * @param username The username to check
     * @return true if username is available, false if already taken
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    /**
     * Checks if an email is available for registration
     * 
     * @param email The email to check
     * @return true if email is available, false if already taken
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    /**
     * Converts User entity to UserDto
     * Excludes sensitive information like password
     * 
     * @param user The user entity to convert
     * @return UserDto without password
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
    
    /**
     * Converts UserDto to User entity
     * Used for creating new users
     * 
     * @param userDto The user DTO to convert
     * @return User entity
     */
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }
}
