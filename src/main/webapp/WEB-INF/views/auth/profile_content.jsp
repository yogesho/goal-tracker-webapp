<!-- Page Header -->
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2">
        <i class="bi bi-person-circle me-2"></i>
        Profile
    </h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <div class="btn-group me-2">
            <a href="/goals" class="btn btn-sm btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>
                Back to Goals
            </a>
        </div>
    </div>
</div>



<!-- Profile Information -->
<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="bi bi-person me-2"></i>
                    Account Information
                </h5>
            </div>
            <div class="card-body">
                <c:if test="${not empty currentUser}">
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <strong>Username:</strong>
                        </div>
                        <div class="col-sm-9">
                            ${currentUser.username}
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <strong>Email:</strong>
                        </div>
                        <div class="col-sm-9">
                            ${currentUser.email}
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <strong>Member Since:</strong>
                        </div>
                        <div class="col-sm-9">
                            ${formattedCreatedAt}
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Account Actions -->
        <div class="card mt-4">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="bi bi-gear me-2"></i>
                    Account Actions
                </h5>
            </div>
            <div class="card-body">
                <div class="d-grid gap-2 d-md-block">
                    <a href="/auth/logout" class="btn btn-danger">
                        <i class="bi bi-box-arrow-right me-2"></i>
                        Logout
                    </a>
                    <a href="/goals" class="btn btn-primary">
                        <i class="bi bi-house-door me-2"></i>
                        Go to Dashboard
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Sidebar Stats -->
    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="bi bi-graph-up me-2"></i>
                    Quick Stats
                </h5>
            </div>
            <div class="card-body">
                <div class="text-center mb-3">
                    <i class="bi bi-person-circle" style="font-size: 3rem; color: #6c757d;"></i>
                </div>
                <div class="text-center">
                    <h6>Welcome back!</h6>
                    <p class="text-muted">Manage your goals and track your progress</p>
                </div>
            </div>
        </div>
    </div>
</div>
