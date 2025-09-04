package com.goaltracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal Entity - Represents a user's goal in the database
 * This entity stores the core goal information including title, description,
 * start/end dates, and provides methods to calculate progress metrics.
 * It has a one-to-many relationship with GoalDay entities.
 */
@Entity
@Table(name = "goal")
public class Goal {

    // Primary key - auto-generated unique identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Goal title - required field for identifying the goal
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(nullable = false, length = 255)
    private String title;

    // Goal description - optional detailed description
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    // Start date of the goal period
    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    // End date of the goal period
    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    // Timestamp when the goal was created
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Many-to-one relationship with User entity
    // Each goal belongs to a specific user for data isolation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One-to-many relationship with GoalDay entities
    // Each goal can have multiple day records for tracking daily progress
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GoalDay> goalDays = new ArrayList<>();

    // Default constructor required by JPA
    public Goal() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with required fields
    public Goal(String title, String description, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = LocalDateTime.now();
    }

    // Getter and Setter methods

    /**
     * Gets the unique identifier of the goal
     * 
     * @return The goal ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the goal
     * 
     * @param id The goal ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of the goal
     * 
     * @return The goal title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the goal
     * 
     * @param title The goal title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the goal
     * 
     * @return The goal description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the goal
     * 
     * @param description The goal description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the start date of the goal period
     * 
     * @return The start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the goal period
     * 
     * @param startDate The start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the goal period
     * 
     * @return The end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the goal period
     * 
     * @param endDate The end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the timestamp when the goal was created
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the goal was created
     * 
     * @param createdAt The creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the list of day records associated with this goal
     * 
     * @return List of GoalDay entities
     */
    public List<GoalDay> getGoalDays() {
        return goalDays;
    }

    /**
     * Sets the list of day records associated with this goal
     * 
     * @param goalDays List of GoalDay entities
     */
    public void setGoalDays(List<GoalDay> goalDays) {
        this.goalDays = goalDays;
    }

    /**
     * Gets the user who owns this goal
     * 
     * @return The user entity
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who owns this goal
     * 
     * @param user The user entity
     */
    public void setUser(User user) {
        this.user = user;
    }

    // Business Logic Methods

    /**
     * Calculates the total number of days in the goal period
     * This includes both start and end dates (inclusive)
     * 
     * @return Total number of days
     */
    public long getTotalDays() {
        // Use ChronoUnit.DAYS to calculate the difference between dates
        // Add 1 to include both start and end dates
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * Calculates the number of completed days for this goal
     * Counts only GoalDay records where completed = true
     * 
     * @return Number of completed days
     */
    public long getCompletedDays() {
        // Stream through goal days and count those that are completed
        return goalDays.stream()
                .filter(GoalDay::isCompleted)
                .count();
    }

    /**
     * Calculates the progress percentage of the goal
     * Returns 0 if total days is 0 to avoid division by zero
     * 
     * @return Progress percentage (0.0 to 100.0)
     */
    public double getProgressPercentage() {
        long total = getTotalDays();
        if (total == 0) {
            return 0.0; // Avoid division by zero
        }

        // Calculate percentage: (completed days / total days) * 100
        return (double) getCompletedDays() / total * 100.0;
    }

    /**
     * Determines if the goal is completed
     * A goal is considered completed if all days are marked as completed
     * and there is at least one day in the goal period
     * 
     * @return true if goal is completed, false otherwise
     */
    public boolean isCompleted() {
        long total = getTotalDays();
        long completed = getCompletedDays();

        // Goal is completed if there are days and all days are completed
        return total > 0 && completed == total;
    }

    /**
     * Calculates the number of remaining days to complete the goal
     * 
     * @return Number of remaining days
     */
    public long getRemainingDays() {
        return getTotalDays() - getCompletedDays();
    }

    // Utility Methods

    /**
     * Adds a day record to this goal
     * This method maintains the bidirectional relationship
     * 
     * @param goalDay The GoalDay entity to add
     */
    public void addGoalDay(GoalDay goalDay) {
        goalDays.add(goalDay);
        goalDay.setGoal(this); // Set the bidirectional relationship
    }

    /**
     * Removes a day record from this goal
     * This method maintains the bidirectional relationship
     * 
     * @param goalDay The GoalDay entity to remove
     */
    public void removeGoalDay(GoalDay goalDay) {
        goalDays.remove(goalDay);
        goalDay.setGoal(null); // Clear the bidirectional relationship
    }

    /**
     * Returns a string representation of the goal
     * Useful for debugging and logging
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalDays=" + getTotalDays() +
                ", completedDays=" + getCompletedDays() +
                ", progressPercentage=" + getProgressPercentage() +
                ", completed=" + isCompleted() +
                '}';
    }
}
