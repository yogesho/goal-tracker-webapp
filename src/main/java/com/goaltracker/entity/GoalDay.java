package com.goaltracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * GoalDay Entity - Represents a single day's completion status for a goal
 * This entity tracks whether a specific day was completed or missed for a goal.
 * It has a many-to-one relationship with the Goal entity.
 * 
 * The field name is 'date' but the column name is 'day_date' to avoid
 * conflicts with the SQL 'DATE' keyword in H2 database.
 */
@Entity
@Table(name = "goal_day")
public class GoalDay {

    // Primary key - auto-generated unique identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with Goal entity
    // Each day record belongs to one goal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    // The specific date this record represents
    // Column name is 'day_date' to avoid SQL keyword conflicts
    @Column(name = "day_date", nullable = false)
    private LocalDate date;

    // Whether this day was completed by the user
    // true = completed, false = missed/not completed
    @Column(nullable = false)
    private boolean completed = false;

    // Timestamp when this day was marked as completed
    // null if the day is not completed
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Timestamp when this record was created
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public GoalDay() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor with required fields
     * 
     * @param goal The goal this day belongs to
     * @param date The specific date this record represents
     */
    public GoalDay(Goal goal, LocalDate date) {
        this.goal = goal;
        this.date = date;
        this.completed = false; // Default to not completed
        this.createdAt = LocalDateTime.now();
    }

    // Getter and Setter methods

    /**
     * Gets the unique identifier of this day record
     * 
     * @return The day record ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this day record
     * 
     * @param id The day record ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the goal this day record belongs to
     * 
     * @return The associated Goal entity
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * Sets the goal this day record belongs to
     * 
     * @param goal The associated Goal entity
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * Gets the specific date this record represents
     * 
     * @return The date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the specific date this record represents
     * 
     * @param date The date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Checks if this day was completed by the user
     * 
     * @return true if completed, false if missed/not completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets the completion status of this day
     * If setting to completed, also sets the completedAt timestamp
     * 
     * @param completed true if completed, false if not completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;

        // If marking as completed, set the completion timestamp
        if (completed) {
            this.completedAt = LocalDateTime.now();
        } else {
            // If marking as not completed, clear the completion timestamp
            this.completedAt = null;
        }
    }

    /**
     * Gets the timestamp when this day was marked as completed
     * 
     * @return The completion timestamp, or null if not completed
     */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    /**
     * Sets the timestamp when this day was marked as completed
     * 
     * @param completedAt The completion timestamp
     */
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Gets the timestamp when this record was created
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when this record was created
     * 
     * @param createdAt The creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business Logic Methods

    /**
     * Checks if this day is in the past
     * Useful for determining if a day can still be completed
     * 
     * @return true if the date is before today, false otherwise
     */
    public boolean isPastDay() {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Checks if this day is today
     * Useful for determining if a day can be toggled
     * 
     * @return true if the date is today, false otherwise
     */
    public boolean isToday() {
        return date.equals(LocalDate.now());
    }

    /**
     * Checks if this day is in the future
     * Useful for determining if a day can be interacted with
     * 
     * @return true if the date is after today, false otherwise
     */
    public boolean isFutureDay() {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Gets the day of the week for this date
     * Useful for display purposes
     * 
     * @return The day of the week (e.g., "MONDAY", "TUESDAY")
     */
    public String getDayOfWeek() {
        return date.getDayOfWeek().toString();
    }

    /**
     * Gets a short day name for this date
     * Useful for calendar display
     * 
     * @return Short day name (e.g., "Mon", "Tue")
     */
    public String getShortDayName() {
        return date.getDayOfWeek().toString().substring(0, 3);
    }

    /**
     * Returns a string representation of this day record
     * Useful for debugging and logging
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        return "GoalDay{" +
                "id=" + id +
                ", goalId=" + (goal != null ? goal.getId() : "null") +
                ", date=" + date +
                ", completed=" + completed +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Checks if this day record is equal to another object
     * Two GoalDay records are equal if they have the same ID
     * 
     * @param obj The object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        GoalDay goalDay = (GoalDay) obj;
        return id != null && id.equals(goalDay.getId());
    }

    /**
     * Generates a hash code for this day record
     * Based on the ID field
     * 
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
