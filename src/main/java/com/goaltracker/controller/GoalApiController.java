package com.goaltracker.controller;

import com.goaltracker.service.GoalService;
import com.goaltracker.entity.GoalDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GoalApiController - REST API endpoints for AJAX requests
 * This controller handles all asynchronous requests from the frontend
 * JavaScript,
 * including calendar day toggling, progress updates, and data retrieval.
 * It returns JSON responses instead of HTML pages.
 */
@RestController
@RequestMapping("/api/goals")
public class GoalApiController {

    // Service dependency for business logic
    @Autowired
    private GoalService goalService;

    /**
     * Toggles the completion status of a specific day for a goal
     * This endpoint is called when user clicks on a calendar day
     * 
     * Business Rules:
     * - Only today's date can be toggled (enforced by service layer)
     * - Missing past days are automatically marked as missed
     * - Returns the new completion status
     * 
     * @param goalId The unique identifier of the goal
     * @param date   The date to toggle in ISO format (YYYY-MM-DD)
     * @return JSON response with success status and completion state
     */
    @PostMapping("/{goalId}/days/{date}/toggle")
    public ResponseEntity<Map<String, Object>> toggleGoalDay(
            @PathVariable Long goalId,
            @PathVariable String date) {

        try {
            // Parse the date string to LocalDate object
            LocalDate localDate = LocalDate.parse(date);

            // Call service method to toggle the day's completion status
            boolean completed = goalService.toggleGoalDay(goalId, localDate);

            // Build success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("completed", completed);
            response.put("message", completed ? "Day marked as completed!" : "Day marked as not completed");

            // Return 200 OK with JSON response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Build error response
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            // Return 400 Bad Request with error details
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Retrieves current progress information for a specific goal
     * This endpoint is called to update progress bars and counters
     * 
     * @param goalId The unique identifier of the goal
     * @return JSON response with progress metrics (percentage, counts, completion
     *         status)
     */
    @GetMapping("/{goalId}/progress")
    public ResponseEntity<Map<String, Object>> getGoalProgress(@PathVariable Long goalId) {
        try {
            // Fetch the goal with calculated progress metrics
            var goal = goalService.getGoalById(goalId);

            // Build response with all progress data
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("progressPercentage", goal.getProgressPercentage());
            response.put("completedDays", goal.getCompletedDays());
            response.put("totalDays", goal.getTotalDays());
            response.put("completed", goal.isCompleted());

            // Return 200 OK with progress data
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Build error response
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            // Return 400 Bad Request with error details
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Retrieves all day records for a specific goal
     * This endpoint is called when initializing the calendar to show completion
     * status
     * 
     * @param goalId The unique identifier of the goal
     * @return JSON response with array of day objects containing completion status
     */
    @GetMapping("/{goalId}/days")
    public ResponseEntity<Map<String, Object>> getGoalDays(@PathVariable Long goalId) {
        try {
            // Fetch all day records for this goal
            List<GoalDay> days = goalService.getGoalDays(goalId);

            // Convert GoalDay entities to simple maps for JSON serialization
            List<Map<String, Object>> daysData = days.stream()
                    .map(day -> {
                        Map<String, Object> dayMap = new HashMap<>();
                        dayMap.put("dayDate", day.getDate().toString()); // Date in ISO format
                        dayMap.put("completed", day.isCompleted()); // Completion status
                        dayMap.put("completedAt", day.getCompletedAt()); // Timestamp of completion
                        return dayMap;
                    })
                    .collect(Collectors.toList());

            // Build success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("days", daysData);

            // Return 200 OK with days data
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Build error response
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());

            // Return 400 Bad Request with error details
            return ResponseEntity.badRequest().body(response);
        }
    }
}
