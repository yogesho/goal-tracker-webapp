package com.goaltracker.controller;

import com.goaltracker.dto.UserDto;
import com.goaltracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * UserController - Handles user authentication and registration
 * This controller manages user registration, login, logout, and session
 * management.
 * It provides web pages for user authentication and handles form submissions.
 */
@Controller
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows the login page
     * Displays a form for users to enter their credentials
     * 
     * @param model Spring MVC model for passing data to the view
     * @return The name of the JSP view to render
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }

    /**
     * Processes login form submission
     * Authenticates user credentials and creates a session
     * 
     * @param username           The username from the login form
     * @param password           The password from the login form
     * @param session            HTTP session for storing user data
     * @param redirectAttributes For passing messages between requests
     * @return Redirect to goals page on success, back to login on failure
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Authenticate user
        var userOpt = userService.authenticateUser(username, password);

        if (userOpt.isPresent()) {
            // Store user in session
            UserDto user = userOpt.get();
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());

            // Add success message
            redirectAttributes.addFlashAttribute("success",
                    "Welcome back, " + user.getUsername() + "!");

            return "redirect:/goals";
        } else {
            // Authentication failed
            redirectAttributes.addFlashAttribute("error",
                    "Invalid username or password");
            return "redirect:/auth/login";
        }
    }

    /**
     * Shows the registration page
     * Displays a form for new users to create accounts
     * 
     * @param model Spring MVC model for passing data to the view
     * @return The name of the JSP view to render
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("pageTitle", "Register");
        return "auth/register";
    }

    /**
     * Processes registration form submission
     * Validates user data and creates a new account
     * 
     * @param userDto            The user data from the registration form
     * @param result             Binding result containing validation errors
     * @param session            HTTP session for storing user data
     * @param redirectAttributes For passing messages between requests
     * @return Redirect to goals page on success, back to register on failure
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Check for validation errors
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Check if passwords match
        if (!userDto.isPasswordMatching()) {
            redirectAttributes.addFlashAttribute("error",
                    "Password and confirm password do not match");
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/auth/register";
        }

        try {
            // Register the user
            UserDto registeredUser = userService.registerUser(userDto);

            // Store user in session
            session.setAttribute("currentUser", registeredUser);
            session.setAttribute("userId", registeredUser.getId());

            // Add success message
            redirectAttributes.addFlashAttribute("success",
                    "Account created successfully! Welcome, " + registeredUser.getUsername() + "!");

            return "redirect:/goals";

        } catch (RuntimeException e) {
            // Registration failed
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/auth/register";
        }
    }

    /**
     * Logs out the current user
     * Clears the session and redirects to login page
     * 
     * @param session            HTTP session to clear
     * @param redirectAttributes For passing messages between requests
     * @return Redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Clear session
        session.invalidate();

        // Add logout message
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully");

        return "redirect:/auth/login";
    }

    /**
     * Shows the user profile page
     * Displays current user information
     * 
     * @param session HTTP session to get current user
     * @param model   Spring MVC model for passing data to the view
     * @return The name of the JSP view to render
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        UserDto currentUser = (UserDto) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // Format the creation date for display
        String formattedDate = "";
        if (currentUser.getCreatedAt() != null) {
            formattedDate = currentUser.getCreatedAt()
                    .format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("formattedCreatedAt", formattedDate);
        model.addAttribute("pageTitle", "Profile - " + currentUser.getUsername());
        return "auth/profile";
    }
}
