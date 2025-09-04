<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="row justify-content-center">
    <div class="col-md-6 col-lg-5">
        <div class="card shadow-lg border-0">
            <div class="card-header bg-success text-white text-center py-3">
                <h4 class="mb-0">
                    <i class="bi bi-person-plus me-2"></i>
                    Create Account
                </h4>
            </div>
            <div class="card-body p-4">
                <!-- Flash Messages -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Registration Form -->
                <form action="${pageContext.request.contextPath}/auth/register" method="post" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="username" class="form-label">
                            <i class="bi bi-person me-1"></i>Username
                        </label>
                        <input type="text" 
                               class="form-control" 
                               id="username" 
                               name="username" 
                               value="${userDto.username}"
                               required 
                               minlength="3"
                               maxlength="50"
                               placeholder="Choose a username (3-50 characters)">
                        <div class="invalid-feedback">
                            Please choose a username between 3 and 50 characters.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="email" class="form-label">
                            <i class="bi bi-envelope me-1"></i>Email Address
                        </label>
                        <input type="email" 
                               class="form-control" 
                               id="email" 
                               name="email" 
                               value="${userDto.email}"
                               required 
                               placeholder="Enter your email address">
                        <div class="invalid-feedback">
                            Please enter a valid email address.
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="password" class="form-label">
                            <i class="bi bi-lock me-1"></i>Password
                        </label>
                        <input type="password" 
                               class="form-control" 
                               id="password" 
                               name="password" 
                               required 
                               minlength="6"
                               placeholder="Create a password (min 6 characters)">
                        <div class="invalid-feedback">
                            Password must be at least 6 characters long.
                        </div>
                    </div>
                    
                    <div class="mb-4">
                        <label for="confirmPassword" class="form-label">
                            <i class="bi bi-lock-fill me-1"></i>Confirm Password
                        </label>
                        <input type="password" 
                               class="form-control" 
                               id="confirmPassword" 
                               name="confirmPassword" 
                               required 
                               placeholder="Confirm your password">
                        <div class="invalid-feedback" id="confirmPasswordFeedback">
                            Please confirm your password.
                        </div>
                    </div>
                    
                    <div class="d-grid">
                        <button type="submit" class="btn btn-success btn-lg">
                            <i class="bi bi-person-check me-2"></i>
                            Create Account
                        </button>
                    </div>
                </form>
                
                <hr class="my-4">
                
                <div class="text-center">
                    <p class="mb-0 text-muted">Already have an account?</p>
                    <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-outline-primary btn-sm mt-2">
                        <i class="bi bi-box-arrow-in-left me-1"></i>
                        Sign In
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Enhanced form validation
(function() {
    'use strict';
    window.addEventListener('load', function() {
        var forms = document.getElementsByClassName('needs-validation');
        var validation = Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                
                // Check if passwords match
                var password = document.getElementById('password');
                var confirmPassword = document.getElementById('confirmPassword');
                var confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');
                
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Passwords do not match');
                    confirmPasswordFeedback.textContent = 'Passwords do not match';
                } else {
                    confirmPassword.setCustomValidity('');
                }
                
                form.classList.add('was-validated');
            }, false);
        });
        
        // Real-time password confirmation validation
        var password = document.getElementById('password');
        var confirmPassword = document.getElementById('confirmPassword');
        var confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');
        
        function validatePasswordMatch() {
            if (confirmPassword.value !== '') {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Passwords do not match');
                    confirmPasswordFeedback.textContent = 'Passwords do not match';
                } else {
                    confirmPassword.setCustomValidity('');
                    confirmPasswordFeedback.textContent = '';
                }
            }
        }
        
        password.addEventListener('input', validatePasswordMatch);
        confirmPassword.addEventListener('input', validatePasswordMatch);
    });
})();
</script>
