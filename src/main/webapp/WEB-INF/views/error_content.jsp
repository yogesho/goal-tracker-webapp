<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="row justify-content-center">
	<div class="col-lg-8">
		<div class="card border-0 shadow-sm">
			<div class="card-body p-4">
				<div class="d-flex align-items-center mb-3">
					<div class="display-6 text-danger me-3"><i class="bi bi-exclamation-octagon-fill"></i></div>
					<div>
						<h3 class="mb-0">Oops! Something went wrong</h3>
						<small class="text-muted">We couldn't process your request.</small>
					</div>
				</div>

				<div class="row g-3">
					<div class="col-md-4">
						<div class="p-3 bg-light rounded">
							<div class="text-muted">Status</div>
							<div class="fw-bold">${status}</div>
						</div>
					</div>
					<div class="col-md-8">
						<div class="p-3 bg-light rounded">
							<div class="text-muted">Path</div>
							<div class="fw-bold">${path}</div>
						</div>
					</div>
				</div>

				<c:if test="${not empty error}">
					<div class="mt-3">
						<div class="text-muted mb-1">Error</div>
						<div class="alert alert-danger mb-0">${error}</div>
					</div>
				</c:if>

				<c:if test="${not empty message}">
					<div class="mt-3">
						<div class="text-muted mb-1">Message</div>
						<div class="alert alert-warning mb-0">${message}</div>
					</div>
				</c:if>

				<div class="mt-4 d-flex gap-2">
					<a href="/goals" class="btn btn-primary"><i class="bi bi-house"></i> Back to Home</a>
					<button type="button" class="btn btn-outline-secondary" onclick="history.back()">
						<i class="bi bi-arrow-left"></i> Go Back
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
