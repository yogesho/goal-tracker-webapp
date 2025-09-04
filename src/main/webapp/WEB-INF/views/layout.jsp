<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Goal Tracker - ${param.pageTitle != null ? param.pageTitle : 'Professional Goal Management'}</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link href="/css/custom.css" rel="stylesheet">
    
    <!-- Custom JavaScript -->
    <script src="/js/calendar.js" defer></script>
    
    <!-- Google Fonts - Poppins -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <!-- Modern Sticky Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container">
            <a class="navbar-brand" href="/goals">
                <i class="bi bi-target"></i> GoalTracker
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/goals">
                            <i class="bi bi-house"></i> All Goals
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/goals/working">
                            <i class="bi bi-play-circle"></i> Working Goals
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/goals/completed">
                            <i class="bi bi-check-circle"></i> Previous Goals
                        </a>
                    </li>
                </ul>
                
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser != null}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                    <i class="bi bi-person-circle"></i> ${sessionScope.currentUser.username}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="/auth/profile">
                                        <i class="bi bi-person me-2"></i>Profile
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/auth/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Logout
                                    </a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/login">
                                    <i class="bi bi-box-arrow-in-right"></i> Login
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/auth/register">
                                    <i class="bi bi-person-plus"></i> Register
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="container my-4">
        <jsp:include page="/WEB-INF/views/${param.content}" />
    </main>

    <!-- Footer -->
    <footer class="py-4 mt-5">
        <div class="container text-center">
            <p class="mb-0">
                <i class="bi bi-heart-fill text-danger"></i> 
                GoalTracker - Professional Goal Management System
            </p>
            <p class="mb-0">
                <small>
                    All rights reserved © 2025 
                    <a href="https://www.linkedin.com/in/yogesh-sontakke" target="_blank" class="text-light text-decoration-none fw-bold">
                        <i class="bi bi-linkedin me-1"></i>YS
                    </a>
                </small>
            </p>
        </div>
    </footer>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script src="/js/calendar.js?v=3"></script>
    
    <!-- Enhanced UI Scripts -->
    <script>
        // Toast utility functions
        function getToastIcon(type) {
            switch (type) {
                case 'success': return 'bi-check-circle-fill text-success';
                case 'error': return 'bi-exclamation-triangle-fill text-danger';
                case 'warning': return 'bi-exclamation-triangle-fill text-warning';
                case 'info': return 'bi-info-circle-fill text-info';
                default: return 'bi-info-circle-fill text-info';
            }
        }

        function getToastTitle(type) {
            switch (type) {
                case 'success': return 'Success!';
                case 'error': return 'Error!';
                case 'warning': return 'Warning!';
                case 'info': return 'Info';
                default: return 'Info';
            }
        }

        function createToastContainer() {
            const container = document.getElementById('toastContainer') || document.createElement('div');
            container.id = 'toastContainer';
            container.className = 'toast-container position-fixed top-0 end-0 p-3';
            container.style.zIndex = '9999';
            if (!document.getElementById('toastContainer')) {
                document.body.appendChild(container);
            }
            return container;
        }

        // Enhanced toast system
        function showToast(message, type = 'info') {
            const toastContainer = createToastContainer();
            
            const toastId = 'toast-' + Date.now();
            const iconClass = getToastIcon(type);
            const title = getToastTitle(type);
            
            const toastHtml = `
                <div class="toast fade-in" id="${toastId}" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="toast-header">
                        <i class="bi ${iconClass} me-2"></i>
                        <strong class="me-auto">${title}</strong>
                        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                    <div class="toast-body">
                        ${message}
                    </div>
                </div>
            `;
            
            toastContainer.insertAdjacentHTML('beforeend', toastHtml);
            
            const toastElement = document.getElementById(toastId);
            const toast = new bootstrap.Toast(toastElement, {
                autohide: true,
                delay: 4000
            });
            
            toast.show();
            
            // Auto-remove after animation
            toastElement.addEventListener('hidden.bs.toast', () => {
                toastElement.remove();
            });
        }

        // Enhanced confetti function for goal completion
        function createConfetti() {
            const colors = ['#0d6efd', '#198754', '#ffc107', '#dc3545', '#0dcaf0', '#6f42c1'];
            const confettiCount = 150;
            
            for (let i = 0; i < confettiCount; i++) {
                setTimeout(() => {
                    const confetti = document.createElement('div');
                    confetti.className = 'confetti';
                    confetti.style.left = Math.random() * 100 + 'vw';
                    confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                    confetti.style.width = (Math.random() * 10 + 5) + 'px';
                    confetti.style.height = (Math.random() * 10 + 5) + 'px';
                    confetti.style.animationDuration = (Math.random() * 3 + 2) + 's';
                    confetti.style.animationDelay = Math.random() * 2 + 's';
                    
                    document.body.appendChild(confetti);
                    
                    // Remove confetti after animation
                    setTimeout(() => {
                        confetti.remove();
                    }, 5000);
                }, i * 20);
            }
        }

        // Initialize tooltips globally
        document.addEventListener('DOMContentLoaded', function() {
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });


    </script>
</body>
</html>
