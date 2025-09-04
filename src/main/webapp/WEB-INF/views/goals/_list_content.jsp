<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- Page Header -->
<div class="row mb-5">
    <div class="col-12">
        <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
            <div class="text-center text-md-start">
                <h1 class="display-4 fw-bold text-primary mb-3">
                    <i class="bi bi-target"></i> ${filterTitle != null ? filterTitle : 'My Goals'}
                </h1>
                <p class="lead text-secondary fs-5">Track your progress and achieve your dreams with our modern goal tracking system</p>
            </div>
            <button class="btn btn-primary btn-lg shadow w-100 w-md-auto" data-bs-toggle="modal" data-bs-target="#newGoalModal">
                <i class="bi bi-plus-circle"></i> New Goal
            </button>
        </div>
    </div>
</div>

<!-- Flash Messages -->
<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show slide-up" role="alert">
        <i class="bi bi-check-circle"></i> ${success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show slide-up" role="alert">
        <i class="bi bi-exclamation-triangle"></i> ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<!-- Goals Grid -->
<div class="row g-4">
    <c:forEach var="goal" items="${goals}" varStatus="status">
        <div class="col-12 col-md-6 col-lg-4">
            <div class="card h-100 fade-in shadow" style="animation-delay: ${status.index * 0.1}s;">
                <div class="card-header">
                    <h5 class="card-title mb-0">
                        <i class="bi bi-bullseye"></i> ${goal.title}
                    </h5>
                </div>
                
                <div class="card-body">
                    <p class="card-text text-muted mb-3">
                        <i class="bi bi-chat-quote"></i> ${goal.description}
                    </p>
                    
                    <div class="row text-center mb-4">
                        <div class="col-4">
                            <div class="border-end">
                                <div class="h3 mb-0 text-success fw-bold">${goal.completedDays}</div>
                                <small class="text-secondary">Completed</small>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="border-end">
                                <div class="h3 mb-0 text-warning fw-bold">${goal.remainingDays}</div>
                                <small class="text-secondary">Remaining</small>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="h3 mb-0 text-info fw-bold">${goal.totalDays}</div>
                            <small class="text-secondary">Total</small>
                        </div>
                    </div>
                    
                    <!-- Progress Bar -->
                    <div class="mb-4">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <small class="text-secondary fw-semibold">Progress</small>
                            <small class="text-primary fw-bold fs-6">
                                <fmt:formatNumber value="${goal.progressPercentage}" maxFractionDigits="1"/>%
                            </small>
                        </div>
                        <div class="progress" style="height: 12px;">
                            <div class="progress-bar" role="progressbar" 
                                 style="width: ${goal.progressPercentage}%" 
                                 aria-valuenow="${goal.progressPercentage}" 
                                 aria-valuemin="0" 
                                 aria-valuemax="100">
                            </div>
                        </div>
                    </div>
                    
                    <!-- Date Range -->
                    <div class="row text-center mb-4">
                        <div class="col-6">
                            <small class="text-secondary d-block">Start Date</small>
                            <span class="fw-semibold text-primary">${goal.startDate}</span>
                        </div>
                        <div class="col-6">
                            <small class="text-secondary d-block">End Date</small>
                            <span class="fw-semibold text-primary">${goal.endDate}</span>
                        </div>
                    </div>
                    
                    <!-- Status Badge -->
                    <div class="text-center mb-4">
                        <c:choose>
                            <c:when test="${goal.progressPercentage >= 100}">
                                <span class="badge bg-success fs-6 px-3 py-2">
                                    <i class="bi bi-check-circle"></i> Completed
                                </span>
                            </c:when>
                            <c:when test="${goal.progressPercentage > 0}">
                                <span class="badge bg-primary fs-6 px-3 py-2">
                                    <i class="bi bi-play-circle"></i> In Progress
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary fs-6 px-3 py-2">
                                    <i class="bi bi-clock"></i> Not Started
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="card-footer bg-transparent">
                    <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
                        <a href="/goals/${goal.id}" class="btn btn-outline-primary shadow-sm w-100 w-md-auto">
                            <i class="bi bi-calendar-check"></i> Track Progress
                        </a>
                        <div class="dropdown w-100 w-md-auto">
                            <button class="btn btn-sm btn-outline-secondary dropdown-toggle w-100" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-three-dots-vertical"></i> Actions
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end w-100">
                                <li><a class="dropdown-item" href="/goals/${goal.id}">
                                    <i class="bi bi-eye"></i> View Details
                                </a></li>
                                <li><a class="dropdown-item" href="/goals/${goal.id}/edit">
                                    <i class="bi bi-pencil"></i> Edit
                                </a></li>
                                <li><hr class="dropdown-divider"></li>
                                <c:if test="${goal.id != null}">
                                    <li>
                                        <form action="${pageContext.request.contextPath}/goals/${goal.id}/delete" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this goal?')">
                                            <button type="submit" class="dropdown-item text-danger" style="border: none; background: none; width: 100%; text-align: left;">
                                                <i class="bi bi-trash"></i> Delete (ID: ${goal.id})
                                            </button>
                                        </form>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<!-- Empty State -->
<c:if test="${empty goals}">
    <div class="text-center py-5">
        <div class="mb-4">
            <div class="display-1 text-primary mb-3">
                <i class="bi bi-target"></i>
            </div>
            <div class="bg-primary-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 120px; height: 120px;">
                <i class="bi bi-target display-4 text-primary"></i>
            </div>
        </div>
        <h3 class="text-primary mb-3 fw-bold">No goals yet</h3>
        <p class="text-secondary mb-4 fs-5">Start your journey by creating your first goal and track your progress!</p>
        <button class="btn btn-primary btn-lg shadow" data-bs-toggle="modal" data-bs-target="#newGoalModal">
            <i class="bi bi-plus-circle"></i> Create Your First Goal
        </button>
    </div>
</c:if>

<!-- New Goal Modal -->
<div class="modal fade" id="newGoalModal" tabindex="-1" aria-labelledby="newGoalModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="newGoalModalLabel">
                    <i class="bi bi-plus-circle"></i> Create New Goal
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            
            <form action="/goals" method="post" class="needs-validation" novalidate>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-12 mb-3">
                            <label for="title" class="form-label">
                                <i class="bi bi-bullseye"></i> Goal Title
                            </label>
                            <input type="text" class="form-control" id="title" name="title" 
                                   value="${newGoal.title}" required 
                                   placeholder="e.g., Learn Spanish in 30 days">
                            <div class="invalid-feedback">
                                Please provide a goal title.
                            </div>
                        </div>
                        
                        <div class="col-12 mb-3">
                            <label for="description" class="form-label">
                                <i class="bi bi-chat-quote"></i> Description
                            </label>
                            <textarea class="form-control" id="description" name="description" 
                                      rows="3" placeholder="Describe your goal...">${newGoal.description}</textarea>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label for="startDate" class="form-label">
                                <i class="bi bi-calendar-plus"></i> Start Date *
                            </label>
                            <input type="date" class="form-control" id="startDate" name="startDate" 
                                   value="${newGoal.startDate}" required>
                            <div class="form-text">
                                <i class="bi bi-info-circle"></i> Click to open calendar picker
                            </div>
                            <div class="invalid-feedback">
                                Please select a start date.
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label for="endDate" class="form-label">
                                <i class="bi bi-calendar-check"></i> End Date *
                            </label>
                            <input type="date" class="form-control" id="endDate" name="endDate" 
                                   value="${newGoal.endDate}" required>
                            <div class="form-text">
                                <i class="bi bi-info-circle"></i> Click to open calendar picker (max 1 year)
                            </div>
                            <div class="invalid-feedback">
                                Please select an end date.
                            </div>
                        </div>
                    </div>
                    
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i>
                        <strong>Goal Duration Limit:</strong> Goals cannot exceed 1 year (365 days) to ensure effective tracking and motivation.
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="bi bi-x-circle"></i> Cancel
                    </button>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-circle"></i> Create Goal
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete confirmation is now handled by browser's confirm dialog -->

<script>
// Form validation
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
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

// Delete functionality is now handled by form submission
// No JavaScript delete function needed



// Date validation
document.addEventListener('DOMContentLoaded', function() {
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');
    
    if (startDate && endDate) {
        // Set minimum date to today for start date
        const today = new Date().toISOString().split('T')[0];
        startDate.min = today;
        
        // Set minimum date to today for end date initially
        endDate.min = today;
        
        // Prevent manual typing in date fields
        startDate.addEventListener('keydown', function(e) {
            e.preventDefault();
            return false;
        });
        
        endDate.addEventListener('keydown', function(e) {
            e.preventDefault();
            return false;
        });
        
        // Prevent paste in date fields
        startDate.addEventListener('paste', function(e) {
            e.preventDefault();
            return false;
        });
        
        endDate.addEventListener('paste', function(e) {
            e.preventDefault();
            return false;
        });
        
        startDate.addEventListener('change', function() {
            const selectedStartDate = this.value;
            if (selectedStartDate) {
                // Set end date minimum to start date
                endDate.min = selectedStartDate;
                
                // If end date is before start date, clear it
                if (endDate.value && endDate.value < selectedStartDate) {
                    endDate.value = '';
                }
            }
        });
        
        endDate.addEventListener('change', function() {
            const selectedEndDate = this.value;
            if (selectedEndDate) {
                // Set start date maximum to end date
                startDate.max = selectedEndDate;
                
                // If start date is after end date, clear it
                if (startDate.value && startDate.value > selectedEndDate) {
                    startDate.value = '';
                }
                
                // Check if goal duration exceeds 1 year (365 days)
                if (startDate.value) {
                    const start = new Date(startDate.value);
                    const end = new Date(selectedEndDate);
                    const diffTime = Math.abs(end - start);
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                    
                    if (diffDays > 365) {
                        alert('Goal duration cannot exceed 1 year (365 days). Please select a closer end date.');
                        this.value = '';
                        return;
                    }
                }
            }
        });
        
        // Form validation
        const form = document.querySelector('form');
        if (form) {
            form.addEventListener('submit', function(event) {
                const startVal = startDate.value;
                const endVal = endDate.value;
                
                if (startVal && endVal) {
                    if (new Date(startVal) >= new Date(endVal)) {
                        event.preventDefault();
                        alert('End date must be greater than start date');
                        return false;
                    }
                }
                
                if (startVal && new Date(startVal) < new Date(today)) {
                    event.preventDefault();
                    alert('Start date cannot be in the past');
                    return false;
                }
                
                // Check if goal duration exceeds 1 year
                if (startVal && endVal) {
                    const start = new Date(startVal);
                    const end = new Date(endVal);
                    const diffTime = Math.abs(end - start);
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                    
                    if (diffDays > 365) {
                        event.preventDefault();
                        alert('Goal duration cannot exceed 1 year (365 days). Please select a closer end date.');
                        return false;
                    }
                }
            });
        }
    }
});
</script>
