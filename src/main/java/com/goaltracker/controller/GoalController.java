package com.goaltracker.controller;

import com.goaltracker.dto.GoalDto;
import com.goaltracker.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;

/**
 * GoalController - Handles all web requests related to goal management
 * This controller manages the main web interface for creating, viewing,
 * editing,
 * and deleting goals. It handles form submissions and page navigation.
 */
@Controller
@RequestMapping("/goals")
public class GoalController {

    // Service dependency for business logic
    @Autowired
    private GoalService goalService;

    /**
     * Displays the main goals listing page
     * Shows all goals with options to filter by working/completed status
     * 
     * @param model   Spring MVC model for passing data to the view
     * @param session HTTP session to get current user
     * @return The name of the JSP view to render
     */
    @GetMapping
    public String listGoals(Model model, HttpSession session) {
        // Get current user from session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            // User not logged in, redirect to login
            return "redirect:/auth/login";
        }

        // Fetch all goals for the current user
        List<GoalDto> goals = goalService.getAllGoalsByUserId(userId);

        // Add goals to the model for the view to access
        model.addAttribute("goals", goals);

        // Add a new empty goal DTO for the "New Goal" form
        model.addAttribute("newGoal", new GoalDto());

        // Set page title for the layout
        model.addAttribute("pageTitle", "My Goals");

        // Return the JSP view name
        return "goals/list";
    }

    /**
     * Displays only working (in-progress) goals
     * Used for the "Working Goals" navigation filter
     * 
     * @param model   Spring MVC model for passing data to the view
     * @param session HTTP session to get current user
     * @return The name of the JSP view to render
     */
    @GetMapping("/working")
    public String listWorkingGoals(Model model, HttpSession session) {
        // Get current user from session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            // User not logged in, redirect to login
            return "redirect:/auth/login";
        }

        // Fetch only working goals (incomplete goals) for the current user
        List<GoalDto> goals = goalService.getWorkingGoalsByUserId(userId);

        // Add goals and metadata to the model
        model.addAttribute("goals", goals);
        model.addAttribute("newGoal", new GoalDto());
        model.addAttribute("pageTitle", "Working Goals");
        model.addAttribute("filterTitle", "Working Goals");

        return "goals/list";
    }

    /**
     * Displays only completed goals
     * Used for the "Previous Goals" navigation filter
     * 
     * @param model   Spring MVC model for passing data to the view
     * @param session HTTP session to get current user
     * @return The name of the JSP view to render
     */
    @GetMapping("/completed")
    public String listPreviousGoals(Model model, HttpSession session) {
        // Get current user from session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            // User not logged in, redirect to login
            return "redirect:/auth/login";
        }

        // Fetch only completed goals for the current user
        List<GoalDto> goals = goalService.getCompletedGoalsByUserId(userId);

        // Add goals and metadata to the model
        model.addAttribute("goals", goals);
        model.addAttribute("newGoal", new GoalDto());
        model.addAttribute("pageTitle", "Previous Goals");
        model.addAttribute("filterTitle", "Previous Goals");

        return "goals/list";
    }

    /**
     * Displays the detailed view of a specific goal
     * Shows goal information, progress, and the interactive calendar
     * 
     * @param id    The unique identifier of the goal to display
     * @param model Spring MVC model for passing data to the view
     * @return The name of the JSP view to render, or redirect if goal not found
     */
    @GetMapping("/{id}")
    public String viewGoal(@PathVariable Long id, Model model) {
        try {
            // Fetch the specific goal by ID
            GoalDto goal = goalService.getGoalById(id);

            // Add goal to the model
            model.addAttribute("goal", goal);
            model.addAttribute("pageTitle", goal.getTitle());

            return "goals/view";
        } catch (RuntimeException e) {
            // If goal not found, redirect to goals list with error message
            return "redirect:/goals";
        }
    }

    /**
     * Handles the creation of a new goal
     * Processes form submission from the "New Goal" modal
     * 
     * @param goalDto            The goal data from the form (with validation)
     * @param result             Binding result containing validation errors
     * @param redirectAttributes For passing messages between requests
     * @param session            HTTP session to get current user
     * @return Redirect to goals list page
     */
    @PostMapping
    public String createGoal(@Valid @ModelAttribute("newGoal") GoalDto goalDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Get current user from session
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            // User not logged in, redirect to login
            return "redirect:/auth/login";
        }

        // Check if there are validation errors
        if (result.hasErrors()) {
            // Build error message from validation errors
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors: ");
            result.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            });

            // Add error message and form data to redirect attributes
            redirectAttributes.addFlashAttribute("error", errorMessage.toString());
            redirectAttributes.addFlashAttribute("newGoal", goalDto);

            return "redirect:/goals";
        }

        try {
            // Create the goal using the service layer with user ID
            GoalDto createdGoal = goalService.createGoal(goalDto, userId);

            // Add success message
            redirectAttributes.addFlashAttribute("success",
                    "Goal '" + createdGoal.getTitle() + "' created successfully!");

        } catch (Exception e) {
            // Handle any errors during goal creation
            redirectAttributes.addFlashAttribute("error",
                    "Failed to create goal: " + e.getMessage());
            redirectAttributes.addFlashAttribute("newGoal", goalDto);
        }

        return "redirect:/goals";
    }

    /**
     * Displays the edit form for a specific goal
     * Shows the goal data in an editable form
     * 
     * @param id    The unique identifier of the goal to edit
     * @param model Spring MVC model for passing data to the view
     * @return The name of the JSP view to render
     */
    @GetMapping("/{id}/edit")
    public String editGoal(@PathVariable Long id, Model model) {
        // Fetch the goal to edit
        GoalDto goal = goalService.getGoalById(id);

        // Add goal to the model for the edit form
        model.addAttribute("goal", goal);
        model.addAttribute("pageTitle", "Edit Goal: " + goal.getTitle());

        return "goals/edit";
    }

    /**
     * Handles the update of an existing goal
     * Processes form submission from the edit form
     * 
     * @param id                 The unique identifier of the goal to update
     * @param goalDto            The updated goal data from the form
     * @param result             Binding result containing validation errors
     * @param redirectAttributes For passing messages between requests
     * @return Redirect to the updated goal's detail page
     */
    @PostMapping("/{id}")
    public String updateGoal(@PathVariable Long id,
            @ModelAttribute("goal") GoalDto goalDto,
            RedirectAttributes redirectAttributes) {

        try {
            // Update the goal using the service layer
            GoalDto updatedGoal = goalService.updateGoal(id, goalDto);

            // Add success message
            redirectAttributes.addFlashAttribute("success",
                    "Goal '" + updatedGoal.getTitle() + "' updated successfully!");

        } catch (Exception e) {
            // Handle any errors during goal update
            redirectAttributes.addFlashAttribute("error",
                    "Failed to update goal: " + e.getMessage());
        }

        return "redirect:/goals/" + id;
    }

    /**
     * Handles the deletion of a goal
     * Removes the goal and all its associated day records
     * 
     * @param id                 The unique identifier of the goal to delete
     * @param redirectAttributes For passing messages between requests
     * @return Redirect to goals list page
     */
    @PostMapping("/{id}/delete")
    public String deleteGoal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Log the deletion attempt for debugging
        System.out.println("Attempting to delete goal with ID: " + id);

        try {
            // Delete the goal using the service layer
            goalService.deleteGoal(id);

            // Add success message
            redirectAttributes.addFlashAttribute("success", "Goal deleted successfully!");

        } catch (Exception e) {
            // Handle any errors during goal deletion
            redirectAttributes.addFlashAttribute("error",
                    "Failed to delete goal: " + e.getMessage());
        }

        return "redirect:/goals";
    }
}
