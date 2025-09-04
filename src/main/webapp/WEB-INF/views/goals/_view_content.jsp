<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- Page Header -->
<div class="row mb-4">
    <div class="col-12">
        <div class="d-flex justify-content-between align-items-start">
            <div>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="/goals" class="text-decoration-none">Goals</a></li>
                        <li class="breadcrumb-item active" aria-current="page">${goal.title}</li>
                    </ol>
                </nav>
                <h1 class="display-5 fw-bold text-primary mb-2">
                    <i class="bi bi-bullseye"></i> ${goal.title}
                </h1>
                <p class="lead text-muted mb-0">${goal.description}</p>
            </div>
            <div class="d-flex gap-2">
                <a href="/goals/${goal.id}/edit" class="btn btn-outline-primary">
                    <i class="bi bi-pencil"></i> Edit
                </a>
                <a href="/goals" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Back
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Goal Status Alert -->
<div class="row mb-4">
    <div class="col-12">
        <div id="goalStatusAlert" class="alert alert-info slide-up" role="alert">
            <i class="bi bi-clock"></i> In Progress<br/>Keep going! You're doing great!
        </div>
    </div>
</div>

<!-- Progress Overview Cards -->
<div class="row g-4 mb-4">
    <div class="col-12 col-md-3">
        <div class="card text-center fade-in" style="animation-delay: 0.1s;">
            <div class="card-body">
                <div class="display-6 text-primary mb-2">
                    <i class="bi bi-check-circle"></i>
                </div>
                <h3 class="card-title text-primary" id="completedDaysValue">${goal.completedDays}</h3>
                <p class="card-text text-muted">Completed Days</p>
            </div>
        </div>
    </div>
    
    <div class="col-12 col-md-3">
        <div class="card text-center fade-in" style="animation-delay: 0.2s;">
            <div class="card-body">
                <div class="display-6 text-warning mb-2">
                    <i class="bi bi-clock"></i>
                </div>
                <h3 class="card-title text-warning" id="remainingDaysValue">${goal.remainingDays}</h3>
                <p class="card-text text-muted">Remaining Days</p>
            </div>
        </div>
    </div>
    
    <div class="col-12 col-md-3">
        <div class="card text-center fade-in" style="animation-delay: 0.3s;">
            <div class="card-body">
                <div class="display-6 text-info mb-2">
                    <i class="bi bi-calendar-range"></i>
                </div>
                <h3 class="card-title text-info" id="totalDaysValue">${goal.totalDays}</h3>
                <p class="card-text text-muted">Total Days</p>
            </div>
        </div>
    </div>
    
    <div class="col-12 col-md-3">
        <div class="card text-center fade-in" style="animation-delay: 0.4s;">
            <div class="card-body">
                <div class="display-6 text-success mb-2">
                    <i class="bi bi-percent"></i>
                </div>
                <h3 class="card-title text-success" id="progressPercentBadge">
                    <fmt:formatNumber value="${goal.progressPercentage}" maxFractionDigits="1"/>%
                </h3>
                <p class="card-text text-muted">Progress</p>
            </div>
        </div>
    </div>
</div>

<!-- Progress Bar Section -->
<div class="row mb-4">
    <div class="col-12">
        <div class="card slide-up">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="bi bi-graph-up"></i> Progress Overview
                </h5>
            </div>
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="text-muted">Goal Progress</span>
                    <span class="text-muted fw-bold" id="progressDaysText">
                        ${goal.completedDays}/${goal.totalDays} days
                    </span>
                </div>
                <div class="progress mb-3" style="height: 12px;">
                    <div class="progress-bar" role="progressbar" 
                         style="width: ${goal.progressPercentage}%" 
                         aria-valuenow="${goal.progressPercentage}" 
                         aria-valuemin="0" 
                         aria-valuemax="100">
                    </div>
                </div>
                <div class="row text-center">
                    <div class="col-6">
                        <small class="text-muted d-block">Start Date</small>
                        <span class="fw-semibold">${goal.startDate}</span>
                    </div>
                    <div class="col-6">
                        <small class="text-muted d-block">End Date</small>
                        <span class="fw-semibold">${goal.endDate}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Calendar Section -->
<div class="row">
    <div class="col-12">
        <div class="card slide-up">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="bi bi-calendar-week"></i> Daily Progress Calendar
                </h5>
            </div>
            <div class="card-body">
                <div id="goalCalendar" class="text-center">
                    <!-- Calendar will be rendered here by JavaScript -->
                    <div class="d-flex justify-content-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Legend -->
<div class="row mt-4">
    <div class="col-12">
        <div class="card">
            <div class="card-body">
                <h6 class="card-title mb-3">
                    <i class="bi bi-info-circle"></i> Calendar Legend
                </h6>
                <div class="row g-3">
                    <div class="col-6 col-md-3">
                        <div class="d-flex align-items-center">
                            <div class="calendar-day completed me-2" style="width: 30px; height: 30px; min-height: auto;">
                                <i class="bi bi-check-circle-fill"></i>
                            </div>
                            <small>Completed</small>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="d-flex align-items-center">
                            <div class="calendar-day today me-2" style="width: 30px; height: 30px; min-height: auto;">
                                <i class="bi bi-calendar-day"></i>
                            </div>
                            <small>Today</small>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="d-flex align-items-center">
                            <div class="calendar-day past-day missed me-2" style="width: 30px; height: 30px; min-height: auto;">
                                <i class="bi bi-x-circle"></i>
                            </div>
                            <small>Missed</small>
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="d-flex align-items-center">
                            <div class="calendar-day future-day me-2" style="width: 30px; height: 30px; min-height: auto;">
                                <i class="bi bi-calendar-plus"></i>
                            </div>
                            <small>Future</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Tips Section -->
<div class="row mt-4">
    <div class="col-12">
        <div class="card bg-light">
            <div class="card-body">
                <h6 class="card-title">
                    <i class="bi bi-lightbulb"></i> Tips for Success
                </h6>
                <div class="row">
                    <div class="col-md-6">
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2">
                                <i class="bi bi-check2 text-success"></i> Mark each day as completed when you work on your goal
                            </li>
                            <li class="mb-2">
                                <i class="bi bi-check2 text-success"></i> Don't worry about missing days - focus on consistency
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-6">
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2">
                                <i class="bi bi-check2 text-success"></i> Celebrate small wins to stay motivated
                            </li>
                            <li class="mb-2">
                                <i class="bi bi-check2 text-success"></i> Review your progress regularly
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Toast Container for Notifications -->
<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="goalToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <i class="bi bi-info-circle text-primary me-2"></i>
            <strong class="me-auto">Goal Tracker</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body" id="toastMessage"></div>
    </div>
</div>



<script>
document.addEventListener('DOMContentLoaded', function() {
    // Initialize calendar with goal data
    initializeCalendar(${goal.id}, '${goal.startDate}', '${goal.endDate}');
    
    // Add smooth scrolling for better UX
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Add hover effects to cards
    document.querySelectorAll('.card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
});

function showToast(message, type = 'info') {
    const toast = document.getElementById('goalToast');
    const toastMessage = document.getElementById('toastMessage');
    toastMessage.textContent = message;
    toast.className = 'toast ' + (type === 'success' ? 'border-success' : (type === 'error' ? 'border-danger' : 'border-primary'));
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
}
</script>
