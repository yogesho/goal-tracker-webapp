<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- Page Header -->
<div class="row mb-4">
    <div class="col">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/goals" class="text-decoration-none">Goals</a></li>
                <li class="breadcrumb-item"><a href="/goals/${goal.id}" class="text-decoration-none">${goal.title}</a></li>
                <li class="breadcrumb-item active" aria-current="page">Edit</li>
            </ol>
        </nav>
        
        <h1 class="display-6 text-primary">
            <i class="bi bi-pencil"></i> Edit Goal
        </h1>
        <p class="lead text-muted">Update your goal details and timeline</p>
    </div>
</div>

<!-- Edit Form -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">
                    <i class="bi bi-gear"></i> Goal Settings
                </h5>
            </div>
            
            <div class="card-body">
                <form action="/goals/${goal.id}" method="post" modelAttribute="goal">
                    <div class="mb-3">
                        <label for="title" class="form-label">Goal Title *</label>
                        <input type="text" class="form-control" id="title" name="title" 
                               value="${goal.title}" required minlength="3" maxlength="100">
                        <div class="form-text">Choose a clear, specific title for your goal</div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="4" 
                                  maxlength="500">${goal.description}</textarea>
                        <div class="form-text">Optional: Add more details about your goal</div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="startDate" class="form-label">Start Date *</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" 
                                       value="${goal.startDate}" required>
                                <div class="form-text">
                                    <i class="bi bi-info-circle"></i> Click to open calendar picker
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="endDate" class="form-label">End Date *</label>
                                <input type="date" class="form-control" id="endDate" name="endDate" 
                                       value="${goal.endDate}" required>
                                <div class="form-text">
                                    <i class="bi bi-info-circle"></i> Click to open calendar picker (max 1 year)
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i>
                        <strong>Note:</strong> Changing the start or end date will reset all progress tracking. 
                        Your current progress will be lost.
                    </div>
                    
                    <div class="alert alert-warning">
                        <i class="bi bi-exclamation-triangle"></i>
                        <strong>Goal Duration Limit:</strong> Goals cannot exceed 1 year (365 days) to ensure effective tracking and motivation.
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="/goals/${goal.id}" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Cancel
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle"></i> Update Goal
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Current Progress Summary -->
        <div class="card border-0 shadow-sm mt-4">
            <div class="card-header bg-light">
                <h6 class="card-title mb-0 text-muted">
                    <i class="bi bi-graph-up"></i> Current Progress
                </h6>
            </div>
            <div class="card-body">
                <div class="row text-center">
                    <div class="col">
                        <h5 class="text-success">${goal.completedDays}</h5>
                        <small class="text-muted">Completed Days</small>
                    </div>
                    <div class="col">
                        <h5 class="text-warning">${goal.totalDays - goal.completedDays}</h5>
                        <small class="text-muted">Remaining Days</small>
                    </div>
                    <div class="col">
                        <h5 class="text-primary"><fmt:formatNumber value="${goal.progressPercentage}" pattern="0.0"/>%</h5>
                        <small class="text-muted">Progress</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    
    if (startDateInput && endDateInput) {
        // Set minimum date to today for start date
        const today = new Date().toISOString().split('T')[0];
        startDateInput.min = today;
        
        // Prevent manual typing in date fields
        startDateInput.addEventListener('keydown', function(e) {
            e.preventDefault();
            return false;
        });
        
        endDateInput.addEventListener('keydown', function(e) {
            e.preventDefault();
            return false;
        });
        
        // Prevent paste in date fields
        startDateInput.addEventListener('paste', function(e) {
            e.preventDefault();
            return false;
        });
        
        endDateInput.addEventListener('paste', function(e) {
            e.preventDefault();
            return false;
        });
        
        startDateInput.addEventListener('change', function() {
            endDateInput.min = this.value;
            if (endDateInput.value && endDateInput.value < this.value) {
                endDateInput.value = this.value;
            }
        });
        
        endDateInput.addEventListener('change', function() {
            startDateInput.max = this.value;
            
            // Check if goal duration exceeds 1 year (365 days)
            if (startDateInput.value) {
                const start = new Date(startDateInput.value);
                const end = new Date(this.value);
                const diffTime = Math.abs(end - start);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                
                if (diffDays > 365) {
                    alert('Goal duration cannot exceed 1 year (365 days). Please select a closer end date.');
                    this.value = '';
                    return;
                }
            }
        });
        
        // Form validation
        const form = document.querySelector('form');
        if (form) {
            form.addEventListener('submit', function(event) {
                const startVal = startDateInput.value;
                const endVal = endDateInput.value;
                
                if (startVal && endVal) {
                    if (new Date(startVal) >= new Date(endVal)) {
                        event.preventDefault();
                        alert('End date must be greater than start date');
                        return false;
                    }
                    
                    // Check if goal duration exceeds 1 year
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
                
                if (startVal && new Date(startVal) < new Date(today)) {
                    event.preventDefault();
                    alert('Start date cannot be in the past');
                    return false;
                }
            });
        }
    }
});
</script>
