package com.goaltracker.service;

import com.goaltracker.dto.GoalDto;
import com.goaltracker.entity.Goal;
import com.goaltracker.entity.GoalDay;
import com.goaltracker.repository.GoalRepository;
import com.goaltracker.repository.GoalDayRepository;
import com.goaltracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GoalService - Core business logic for goal management
 * This service handles all goal-related operations including CRUD operations,
 * progress tracking, and day completion management.
 */
@Service
@Transactional
public class GoalService {

    // Repository dependencies for database operations
    @Autowired
    private GoalRepository goalRepository; // Handles Goal entity operations

    @Autowired
    private GoalDayRepository goalDayRepository; // Handles GoalDay entity operations

    @Autowired
    private UserService userService; // Handles User entity operations

    /**
     * Retrieves all goals for a specific user from database with detailed logging
     * for debugging
     * This method is used for the main goals listing page
     * 
     * @param userId The ID of the user whose goals to retrieve
     * @return List of GoalDto objects representing all goals for the user
     */
    public List<GoalDto> getAllGoalsByUserId(Long userId) {
        // Fetch all goals for the user ordered by creation date (newest first)
        List<Goal> goals = goalRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        // Debug logging to track goal retrieval
        System.out.println("=== GOAL RETRIEVAL DEBUG ===");
        System.out.println("Found " + goals.size() + " goals in database for user " + userId);

        // Convert Goal entities to GoalDto objects for frontend consumption
        List<GoalDto> goalDtos = goals.stream()
                .map(this::convertToDto) // Use convertToDto method for each goal
                .collect(Collectors.toList());

        // Log each goal's details for debugging purposes
        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i);
            GoalDto dto = goalDtos.get(i);
            System.out.println("Goal " + (i + 1) + ":");
            System.out.println("  - Entity ID: " + goal.getId());
            System.out.println("  - DTO ID: " + dto.getId());
            System.out.println("  - Title: " + goal.getTitle());
        }
        System.out.println("=== END GOAL RETRIEVAL DEBUG ===");

        return goalDtos;
    }

    /**
     * Retrieves all goals from database with detailed logging for debugging
     * This method is used for the main goals listing page (legacy method)
     * 
     * @return List of GoalDto objects representing all goals
     */
    public List<GoalDto> getAllGoals() {
        // Fetch all goals ordered by creation date (newest first)
        List<Goal> goals = goalRepository.findAllOrderByCreatedAtDesc();

        // Debug logging to track goal retrieval
        System.out.println("=== GOAL RETRIEVAL DEBUG ===");
        System.out.println("Found " + goals.size() + " goals in database");

        // Convert Goal entities to GoalDto objects for frontend consumption
        List<GoalDto> goalDtos = goals.stream()
                .map(this::convertToDto) // Use convertToDto method for each goal
                .collect(Collectors.toList());

        // Log each goal's details for debugging purposes
        for (int i = 0; i < goals.size(); i++) {
            Goal goal = goals.get(i);
            GoalDto dto = goalDtos.get(i);
            System.out.println("Goal " + (i + 1) + ":");
            System.out.println("  - Entity ID: " + goal.getId());
            System.out.println("  - DTO ID: " + dto.getId());
            System.out.println("  - Title: " + goal.getTitle());
        }
        System.out.println("=== END GOAL RETRIEVAL DEBUG ===");

        return goalDtos;
    }

    /**
     * Retrieves only working (in-progress) goals
     * A goal is considered "working" if completedDays < totalDays
     * Used for the "Working Goals" filter in navigation
     * 
     * @return List of GoalDto objects for in-progress goals only
     */
    public List<GoalDto> getWorkingGoals() {
        return goalRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDtoUsingCounts) // Use counts-based conversion for accurate progress
                .filter(dto -> dto.getCompletedDays() < dto.getTotalDays()) // Filter for incomplete goals
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only completed goals
     * A goal is considered "completed" if completedDays == totalDays and totalDays
     * > 0
     * Used for the "Previous Goals" filter in navigation
     * 
     * @return List of GoalDto objects for completed goals only
     */
    public List<GoalDto> getCompletedGoals() {
        return goalRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDtoUsingCounts) // Use counts-based conversion for accurate progress
                .filter(dto -> dto.getTotalDays() > 0 && dto.getCompletedDays() == dto.getTotalDays()) // Filter for
                                                                                                       // completed
                                                                                                       // goals
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only working (in-progress) goals for a specific user
     * A goal is considered "working" if completedDays < totalDays
     * Used for the "Working Goals" filter in navigation
     * 
     * @param userId The ID of the user whose goals to retrieve
     * @return List of GoalDto objects for in-progress goals only
     */
    public List<GoalDto> getWorkingGoalsByUserId(Long userId) {
        return goalRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDtoUsingCounts) // Use counts-based conversion for accurate progress
                .filter(dto -> dto.getCompletedDays() < dto.getTotalDays()) // Filter for incomplete goals
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only completed goals for a specific user
     * A goal is considered "completed" if completedDays == totalDays and totalDays
     * > 0
     * Used for the "Previous Goals" filter in navigation
     * 
     * @param userId The ID of the user whose goals to retrieve
     * @return List of GoalDto objects for completed goals only
     */
    public List<GoalDto> getCompletedGoalsByUserId(Long userId) {
        return goalRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDtoUsingCounts) // Use counts-based conversion for accurate progress
                .filter(dto -> dto.getTotalDays() > 0 && dto.getCompletedDays() == dto.getTotalDays()) // Filter for
                                                                                                       // completed
                                                                                                       // goals
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific goal by its ID
     * Used when viewing individual goal details
     * 
     * @param id The unique identifier of the goal
     * @return GoalDto object representing the goal
     * @throws RuntimeException if goal is not found
     */
    public GoalDto getGoalById(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + id));
        return convertToDto(goal);
    }

    /**
     * Creates a new goal and automatically generates all goal days
     * This method is called when user submits the "New Goal" form
     * 
     * @param goalDto The goal data from the form
     * @param userId  The ID of the user creating the goal
     * @return GoalDto object representing the created goal
     */
    public GoalDto createGoal(GoalDto goalDto, Long userId) {
        // Get the user entity
        var userOpt = userService.getUserEntityById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Create new Goal entity from DTO data
        Goal goal = new Goal();
        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setStartDate(goalDto.getStartDate());
        goal.setEndDate(goalDto.getEndDate());
        goal.setUser(userOpt.get()); // Associate with user

        // Save the goal to database first to get the generated ID
        Goal savedGoal = goalRepository.save(goal);

        // Create individual day records for the entire goal period
        // This allows tracking completion status for each day
        createGoalDays(savedGoal);

        return convertToDto(savedGoal);
    }

    /**
     * Creates a new goal and automatically generates all goal days
     * This method is called when user submits the "New Goal" form (legacy method)
     * 
     * @param goalDto The goal data from the form
     * @return GoalDto object representing the created goal
     */
    public GoalDto createGoal(GoalDto goalDto) {
        // Create new Goal entity from DTO data
        Goal goal = new Goal();
        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setStartDate(goalDto.getStartDate());
        goal.setEndDate(goalDto.getEndDate());

        // Save the goal to database first to get the generated ID
        Goal savedGoal = goalRepository.save(goal);

        // Create individual day records for the entire goal period
        // This allows tracking completion status for each day
        createGoalDays(savedGoal);

        return convertToDto(savedGoal);
    }

    /**
     * Updates an existing goal and recreates all goal days
     * This method is called when user edits a goal
     * 
     * @param id      The unique identifier of the goal to update
     * @param goalDto The updated goal data
     * @return GoalDto object representing the updated goal
     * @throws RuntimeException if goal is not found
     */
    public GoalDto updateGoal(Long id, GoalDto goalDto) {
        // Find existing goal or throw exception
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + id));

        // Update goal properties with new values
        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setStartDate(goalDto.getStartDate());
        goal.setEndDate(goalDto.getEndDate());

        // Save the updated goal
        Goal savedGoal = goalRepository.save(goal);

        // If dates changed, we need to recreate all goal days
        // Delete existing days and create new ones for the new date range
        goalDayRepository.deleteByGoalId(id);
        createGoalDays(savedGoal);

        return convertToDto(savedGoal);
    }

    /**
     * Deletes a goal and all its associated day records
     * This method is called when user deletes a goal
     * 
     * @param id The unique identifier of the goal to delete
     */
    public void deleteGoal(Long id) {
        // First delete all associated goal days to avoid foreign key constraint issues
        goalDayRepository.deleteByGoalId(id);
        // Then delete the goal itself
        goalRepository.deleteById(id);
    }

    /**
     * Toggles the completion status of a specific day for a goal
     * This is the core method for the calendar day clicking functionality
     * 
     * Business Rules:
     * 1. Only today's date can be toggled (prevents cheating)
     * 2. Missing past days are automatically marked as missed
     * 3. Each day can be toggled between completed and not completed
     * 
     * @param goalId The unique identifier of the goal
     * @param date   The date to toggle (must be today)
     * @return boolean indicating if the day is now completed
     * @throws RuntimeException if date is not today or goal not found
     */
    public boolean toggleGoalDay(Long goalId, LocalDate date) {
        // Find the goal or throw exception
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));

        // CRITICAL BUSINESS RULE: Only today's date can be toggled
        // This prevents users from cheating by marking past days as completed
        LocalDate today = LocalDate.now();
        if (!date.equals(today)) {
            throw new RuntimeException("Only today's date can be updated");
        }

        // AUTOMATIC BACKFILL: Create missed day records for any past days
        // This ensures all past days are accounted for, even if user missed them
        LocalDate prev = goal.getStartDate();
        while (prev.isBefore(date)) {
            var prevDayOpt = goalDayRepository.findByGoalIdAndDate(goalId, prev);
            if (prevDayOpt.isEmpty()) {
                // Create a missed day record (completed = false)
                GoalDay missed = new GoalDay(goal, prev);
                missed.setCompleted(false); // Mark as missed (will show as red X in UI)
                goalDayRepository.save(missed);
            }
            prev = prev.plusDays(1);
        }

        // Find or create the goal day record for today
        GoalDay goalDay = goalDayRepository.findByGoalIdAndDate(goalId, date)
                .orElseGet(() -> {
                    // If no record exists for today, create one
                    GoalDay newDay = new GoalDay(goal, date);
                    return goalDayRepository.save(newDay);
                });

        // Toggle the completion status (true becomes false, false becomes true)
        goalDay.setCompleted(!goalDay.isCompleted());
        goalDayRepository.save(goalDay);

        return goalDay.isCompleted();
    }

    /**
     * Retrieves all day records for a specific goal
     * Used by the frontend to display calendar completion status
     * 
     * @param goalId The unique identifier of the goal
     * @return List of GoalDay objects representing each day of the goal
     */
    public List<GoalDay> getGoalDays(Long goalId) {
        return goalDayRepository.findByGoalIdOrderByDayDate(goalId);
    }

    /**
     * Creates individual day records for the entire goal period
     * This method is called when creating or updating a goal
     * Each day gets its own record for tracking completion status
     * 
     * @param goal The goal entity for which to create days
     */
    private void createGoalDays(Goal goal) {
        LocalDate currentDate = goal.getStartDate();
        LocalDate endDate = goal.getEndDate();

        // Create a GoalDay record for each day in the goal period
        while (!currentDate.isAfter(endDate)) {
            GoalDay goalDay = new GoalDay(goal, currentDate);
            goalDayRepository.save(goalDay);
            currentDate = currentDate.plusDays(1);
        }
    }

    /**
     * Converts a Goal entity to GoalDto for frontend consumption
     * This method uses entity methods to calculate progress
     * Used for individual goal operations
     * 
     * @param goal The Goal entity to convert
     * @return GoalDto object with calculated progress
     */
    private GoalDto convertToDto(Goal goal) {
        // Debug logging to track conversion process
        System.out.println("=== CONVERT TO DTO DEBUG ===");
        System.out.println("Converting Goal Entity to DTO:");
        System.out.println("  - Goal Entity ID: " + goal.getId());
        System.out.println("  - Goal Entity ID type: "
                + (goal.getId() != null ? goal.getId().getClass().getSimpleName() : "null"));

        // Create new DTO and populate with entity data
        GoalDto dto = new GoalDto();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setStartDate(goal.getStartDate());
        dto.setEndDate(goal.getEndDate());

        // Calculate progress metrics using entity methods
        dto.setTotalDays(goal.getTotalDays());
        dto.setCompletedDays(goal.getCompletedDays());
        dto.setRemainingDays(goal.getTotalDays() - goal.getCompletedDays());
        dto.setProgressPercentage(goal.getProgressPercentage());
        dto.setCompleted(goal.isCompleted());

        // Debug logging for verification
        System.out.println("  - DTO ID after setting: " + dto.getId());
        System.out
                .println("  - DTO ID type: " + (dto.getId() != null ? dto.getId().getClass().getSimpleName() : "null"));
        System.out.println("  - DTO Title: " + dto.getTitle());
        System.out.println("=== END CONVERT TO DTO DEBUG ===");

        return dto;
    }

    /**
     * Converts a Goal entity to GoalDto using database counts
     * This method uses direct database queries for more accurate progress
     * calculation
     * Used for goal listing operations where we need precise counts
     * 
     * @param goal The Goal entity to convert
     * @return GoalDto object with database-calculated progress
     */
    private GoalDto convertToDtoUsingCounts(Goal goal) {
        // Create new DTO and populate with entity data
        GoalDto dto = new GoalDto();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setStartDate(goal.getStartDate());
        dto.setEndDate(goal.getEndDate());

        // Use direct database queries for accurate counts
        long total = goalDayRepository.countTotalDaysByGoalId(goal.getId());
        long completed = goalDayRepository.countCompletedDaysByGoalId(goal.getId());

        // Set calculated progress metrics
        dto.setTotalDays(total);
        dto.setCompletedDays(completed);
        dto.setRemainingDays(total - completed);

        // Calculate percentage (avoid division by zero)
        dto.setProgressPercentage(total == 0 ? 0.0 : (double) completed / total * 100.0);

        // Determine if goal is completed (must have days and all must be completed)
        dto.setCompleted(total > 0 && completed == total);

        return dto;
    }
}
