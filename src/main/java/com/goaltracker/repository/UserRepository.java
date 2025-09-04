package com.goaltracker.repository;

import com.goaltracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Data access layer for User entity
 * This repository provides methods to interact with the users table
 * and includes custom queries for user authentication and management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by their unique username
     * Used for login authentication
     * 
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Finds a user by their unique email address
     * Used for email-based authentication and validation
     * 
     * @param email The email address to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Checks if a username already exists in the database
     * Used for registration validation to prevent duplicate usernames
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if an email address already exists in the database
     * Used for registration validation to prevent duplicate emails
     * 
     * @param email The email address to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Custom query to find users with their associated goals
     * Uses JOIN FETCH to eagerly load goals and avoid N+1 problem
     * 
     * @param userId The ID of the user to find
     * @return Optional containing the user with goals if found
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.goals WHERE u.id = :userId")
    Optional<User> findByIdWithGoals(@Param("userId") Long userId);
    
    /**
     * Custom query to find a user by username with their associated goals
     * Used when we need user data along with their goals
     * 
     * @param username The username to search for
     * @return Optional containing the user with goals if found
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.goals WHERE u.username = :username")
    Optional<User> findByUsernameWithGoals(@Param("username") String username);
}
