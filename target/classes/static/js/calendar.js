/**
 * Modern Goal Tracker Calendar JavaScript
 * Professional SaaS Dashboard with Enhanced Animations
 * 
 * This file contains all the frontend logic for the goal tracking calendar,
 * including calendar rendering, day completion toggling, progress updates,
 * and user interface animations.
 */

// Calendar rendering and interactions for Goal Tracker

/**
 * Initializes and renders the goal progress calendar
 * This is the main function that creates the entire calendar interface
 * 
 * @param {number} goalId - The unique identifier of the goal
 * @param {string} startDateStr - Start date in ISO format (YYYY-MM-DD)
 * @param {string} endDateStr - End date in ISO format (YYYY-MM-DD)
 */
async function initializeCalendar(goalId, startDateStr, endDateStr) {
    // Get the calendar container element from the DOM
    const container = document.getElementById('goalCalendar');
    if (!container) return; // Exit if container doesn't exist

    // Convert string dates to Date objects for manipulation
    const startDate = new Date(startDateStr);
    const endDate = new Date(endDateStr);

    // Fetch current progress and list of completed days from the server
    // This ensures we show the correct completion status for each day
    let completedDays = [];
    try {
        // Make API call to get all days for this goal with their completion status
        const daysRes = await fetch(`/api/goals/${goalId}/days`);
        if (daysRes.ok) {
            const daysData = await daysRes.json();
            completedDays = daysData.days || []; // Extract days array or use empty array as fallback
        }
    } catch (e) {
        console.error('Error fetching goal days:', e);
    }

    // Build array of all dates between start and end date (inclusive)
    const dates = [];
    let current = new Date(startDate);
    while (current <= endDate) {
        dates.push(new Date(current));
        current.setDate(current.getDate() + 1); // Move to next day
    }

    // Clear the container and start fresh
    container.innerHTML = '';

    // Create the main calendar container with fade-in animation
    const calendarContainer = document.createElement('div');
    calendarContainer.className = 'calendar-container fade-in';

    // Create calendar header with title and description
    const calendarHeader = document.createElement('div');
    calendarHeader.className = 'calendar-header';
    calendarHeader.innerHTML = `
        <h3>📅 Goal Progress Calendar</h3>
        <p>Track your daily progress and stay motivated!</p>
    `;

    // Create the calendar grid that will hold all the day buttons
    const calendarGrid = document.createElement('div');
    calendarGrid.className = 'calendar-grid slide-up';

    // Get today's date for comparison (set to midnight for accurate comparison)
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // Group dates into weeks for proper calendar layout
    const weeks = chunkByWeek(dates);

    // Render each week as a row in the calendar
    weeks.forEach((week, weekIndex) => {
        // Create a row container for this week
        const weekRow = document.createElement('div');
        weekRow.className = 'calendar-week';
        weekRow.style.animationDelay = `${weekIndex * 0.1}s`; // Stagger animation
        weekRow.classList.add('slide-up');

        // Render each day in this week
        week.forEach((date, dayIndex) => {
            // Create a button element for each day
            const dayBtn = document.createElement('button');
            dayBtn.type = 'button';
            dayBtn.dataset.date = toIsoDate(date); // Store date for API calls
            dayBtn.className = 'calendar-day btn fade-in';
            dayBtn.style.animationDelay = `${(weekIndex * 7 + dayIndex) * 0.05}s`; // Stagger animation

            // Format the date for display
            // Get day name (e.g., "Mon", "Tue")
            const dayName = date.toLocaleDateString('en-US', { weekday: 'short' });
            // Get full date format (e.g., "09/09/2024")
            const formattedDate = date.toLocaleDateString('en-US', {
                month: '2-digit',
                day: '2-digit',
                year: 'numeric'
            });
            // Get short date format for responsive display (e.g., "Sep 09")
            const shortDate = date.toLocaleDateString('en-US', {
                month: 'short',
                day: '2-digit'
            });

            // Set the initial HTML content with day name and short date
            dayBtn.innerHTML = `<div class="day-name">${dayName}</div><div class="day-date" data-full-date="${formattedDate}">${shortDate}</div>`;

            // Add tooltip for the entire day button
            const tooltipText = getTooltipText(date, today);
            dayBtn.setAttribute('data-bs-toggle', 'tooltip');
            dayBtn.setAttribute('data-bs-placement', 'top');
            dayBtn.setAttribute('data-bs-title', tooltipText);

            // Add separate tooltip for the date element to show full date on hover
            const dateElement = dayBtn.querySelector('.day-date');
            if (dateElement) {
                dateElement.setAttribute('data-bs-toggle', 'tooltip');
                dateElement.setAttribute('data-bs-placement', 'top');
                dateElement.setAttribute('data-bs-title', formattedDate);
            }

            // Normalize date to midnight for accurate comparison
            const dateMid = new Date(date);
            dateMid.setHours(0, 0, 0, 0);

            // Check if this specific day is marked as completed in the database
            const dayDateStr = toIsoDate(date);
            const isCompleted = completedDays.some(day => day.dayDate === dayDateStr && day.completed === true);

            // Apply different styling and behavior based on date type
            if (dateMid.getTime() < today.getTime()) {
                // PAST DAY: Always disabled, show completion status
                dayBtn.classList.add('past-day');
                dayBtn.disabled = true; // Make it non-clickable

                if (isCompleted) {
                    // Show green checkmark for completed past days
                    dayBtn.classList.add('completed');
                    dayBtn.innerHTML = '<div style="font-size: 2.5rem; font-weight: bold; color: #198754; display: flex; align-items: center; justify-content: center; height: 100%; width: 100%;">✓</div>';
                } else {
                    // Show red cross for missed past days
                    dayBtn.classList.add('missed');
                    dayBtn.innerHTML = '<div style="font-size: 2.5rem; font-weight: bold; color: #dc3545; display: flex; align-items: center; justify-content: center; height: 100%; width: 100%;">✗</div>';
                }
            } else if (dateMid.getTime() === today.getTime()) {
                // TODAY: Always clickable, can be toggled
                dayBtn.classList.add('today');

                if (isCompleted) {
                    // Show green checkmark if today is completed
                    dayBtn.classList.add('completed');
                    dayBtn.innerHTML = '<div style="font-size: 2.5rem; font-weight: bold; color: #198754; display: flex; align-items: center; justify-content: center; height: 100%; width: 100%;">✓</div>';
                } else {
                    // Show normal day format if today is not completed
                    dayBtn.innerHTML = `<div class="day-name">${dayName}</div><div class="day-date">${formattedDate}</div>`;
                }

                // Add click event listener for toggling completion
                dayBtn.addEventListener('click', (e) => {
                    console.log('Day button clicked:', e.target.dataset.date);
                    toggleDay(goalId, dayBtn);
                });
            } else {
                // FUTURE DAY: Disabled but clickable to show "coming soon" message
                dayBtn.classList.add('future-day');
                dayBtn.disabled = false;

                // Add click event listener to show future day message
                dayBtn.addEventListener('click', () => showFutureDayMessage(date));
            }

            // Add this day button to the week row
            weekRow.appendChild(dayBtn);
        });

        // Add this week row to the calendar grid
        calendarGrid.appendChild(weekRow);
    });

    // Assemble the complete calendar by adding header and grid to container
    calendarContainer.appendChild(calendarHeader);
    calendarContainer.appendChild(calendarGrid);
    container.appendChild(calendarContainer);

    // Initialize Bootstrap tooltips for all elements with data-bs-toggle="tooltip"
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Generates appropriate tooltip text based on the date type
 * 
 * @param {Date} date - The date to generate tooltip for
 * @param {Date} today - Today's date for comparison
 * @return {string} Tooltip text
 */
function getTooltipText(date, today) {
    const dateMid = new Date(date);
    dateMid.setHours(0, 0, 0, 0);

    if (dateMid.getTime() < today.getTime()) {
        return 'Past day - Click to view details';
    } else if (dateMid.getTime() === today.getTime()) {
        return 'Today - Click to mark as completed';
    } else {
        return 'Future day - Coming soon!';
    }
}

/**
 * Groups an array of dates into weeks for calendar layout
 * This ensures proper 7-day grid structure
 * 
 * @param {Array} dates - Array of Date objects
 * @return {Array} Array of week arrays, each containing 7 or fewer dates
 */
function chunkByWeek(dates) {
    if (dates.length === 0) return [];

    const result = [];
    let week = [];
    let previous = dates[0];

    for (const d of dates) {
        if (week.length === 0) {
            // Start a new week
            week.push(d);
            previous = d;
            continue;
        }

        const prevW = previous.getDay(); // Day of week (0-6)
        const curW = d.getDay(); // Day of week (0-6)
        const diff = Math.round((d - previous) / 86400000); // Days difference

        // Start new week if:
        // 1. Current day is earlier in week than previous day (e.g., Sun after Sat)
        // 2. There's a gap of more than 1 day
        if (curW < prevW || diff > 1) {
            result.push(week);
            week = [];
        }

        week.push(d);
        previous = d;
    }

    // Add the last week if it has any days
    if (week.length) result.push(week);
    return result;
}

/**
 * Toggles the completion status of a specific day
 * This is the main function called when user clicks on a calendar day
 * 
 * @param {number} goalId - The unique identifier of the goal
 * @param {HTMLElement} buttonEl - The button element that was clicked
 */
async function toggleDay(goalId, buttonEl) {
    // Extract the date from the button's data attribute
    const dateStr = buttonEl.dataset.date;
    // Check if the day was previously completed
    const wasCompleted = buttonEl.classList.contains('completed');

    console.log('Toggle day:', dateStr, 'was completed:', wasCompleted);

    // Add loading state to provide visual feedback
    buttonEl.classList.add('loading');

    // Disable the button to prevent multiple clicks
    buttonEl.disabled = true;

    try {
        // Make API call to toggle the day's completion status
        const res = await fetch(`/api/goals/${goalId}/days/${dateStr}/toggle`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });
        const data = await res.json();
        console.log('Server response:', data);

        // Check if the request was successful
        if (!res.ok || !data.success) throw new Error(data.error || 'Toggle failed');

        // Update the visual state based on server response immediately
        setCompleted(buttonEl, data.completed);
        buttonEl.classList.add('success-pulse');

        // Show appropriate toast notification
        if (data.completed) {
            showToast('✅ Day marked as completed!', 'success');
        } else {
            showToast('🔄 Day marked as incomplete', 'info');
        }

        // Refresh progress indicators immediately
        await refreshProgress(goalId);

        console.log('Toggle completed successfully');

        // Refresh the page to show updated data
        console.log('About to refresh the page...');
        setTimeout(() => {
            console.log('Refreshing page now...');
            // Force page refresh using multiple methods
            try {
                // Method 1: Direct reload
                window.location.reload(true);
            } catch (e) {
                console.error('Method 1 failed:', e);
                try {
                    // Method 2: Redirect to same URL
                    window.location.href = window.location.href;
                } catch (e2) {
                    console.error('Method 2 failed:', e2);
                    // Method 3: Replace current location
                    window.location.replace(window.location.href);
                }
            }
        }, 1000); // 1 second delay to ensure toast is visible
    } catch (err) {
        console.error('Error toggling day:', err);
        showToast('❌ Failed to update day: ' + err.message, 'error');

        // Revert the visual state on error
        setCompleted(buttonEl, wasCompleted);
    } finally {
        // Remove loading state and re-enable button
        buttonEl.classList.remove('loading');
        buttonEl.disabled = false;

        // Remove success pulse animation after delay
        setTimeout(() => {
            buttonEl.classList.remove('success-pulse');
        }, 600);
    }
}

/**
 * Updates the visual appearance of a calendar day based on completion status
 * This function handles the visual changes when a day is marked complete/incomplete
 * 
 * @param {HTMLElement} el - The calendar day button element
 * @param {boolean} completed - Whether the day is now completed
 */
function setCompleted(el, completed) {
    console.log('setCompleted called:', completed, 'for element:', el.dataset.date);

    // Toggle CSS classes for styling
    el.classList.toggle('completed', completed);
    el.classList.toggle('incomplete', !completed);

    if (completed) {
        // Show large green checkmark for completed days
        el.innerHTML = '<div style="font-size: 2.5rem; font-weight: bold; color: #198754; display: flex; align-items: center; justify-content: center; height: 100%; width: 100%;">✓</div>';
        console.log('Applied completed checkmark to:', el.dataset.date);
    } else {
        // Restore the original day name and date format for incomplete days
        const date = new Date(el.dataset.date);
        const dayName = date.toLocaleDateString('en-US', { weekday: 'short' });
        const formattedDate = date.toLocaleDateString('en-US', {
            month: '2-digit',
            day: '2-digit',
            year: 'numeric'
        });
        const shortDate = date.toLocaleDateString('en-US', {
            month: 'short',
            day: '2-digit'
        });
        el.innerHTML = `<div class="day-name">${dayName}</div><div class="day-date" data-full-date="${formattedDate}">${shortDate}</div>`;
        console.log('Restored normal format for:', el.dataset.date);
    }
}

/**
 * Refreshes all progress indicators after a day completion change
 * This updates progress bars, counters, and status messages
 * 
 * @param {number} goalId - The unique identifier of the goal
 */
async function refreshProgress(goalId) {
    try {
        console.log('Refreshing progress for goal:', goalId);

        // Fetch updated progress data from server
        const res = await fetch(`/api/goals/${goalId}/progress`);
        const data = await res.json();

        console.log('Progress API response:', data);

        if (!res.ok || !data.success) {
            console.error('Progress API failed:', data.error);
            return;
        }

        // Update progress bars immediately without animation for instant feedback
        const progressBars = document.querySelectorAll('.progress-bar');
        progressBars.forEach(pb => {
            pb.style.transition = 'none'; // Remove transition for instant update
            pb.style.width = `${data.progressPercentage}%`;
            pb.setAttribute('aria-valuenow', `${data.progressPercentage}`);
            // Restore transition after instant update
            setTimeout(() => {
                pb.style.transition = 'width 0.3s ease-in-out';
            }, 10);
        });

        // Update progress text immediately
        const progressDaysText = document.getElementById('progressDaysText');
        if (progressDaysText) {
            console.log('Updating progressDaysText from', progressDaysText.textContent, 'to', `${data.completedDays}/${data.totalDays} days`);
            progressDaysText.textContent = `${data.completedDays}/${data.totalDays} days`;
        } else {
            console.warn('progressDaysText element not found');
        }

        // Update percentage badge immediately
        const percentBadge = document.getElementById('progressPercentBadge');
        if (percentBadge) {
            console.log('Updating progressPercentBadge from', percentBadge.textContent, 'to', `${(Math.round(data.progressPercentage * 10) / 10).toFixed(1)}%`);
            percentBadge.textContent = `${(Math.round(data.progressPercentage * 10) / 10).toFixed(1)}%`;
        } else {
            console.warn('progressPercentBadge element not found');
        }

        // Update individual counters immediately
        const completedEl = document.getElementById('completedDaysValue');
        const remainingEl = document.getElementById('remainingDaysValue');
        const totalEl = document.getElementById('totalDaysValue');

        if (completedEl) {
            console.log('Updating completedDaysValue from', completedEl.textContent, 'to', data.completedDays);
            completedEl.textContent = data.completedDays;
            // Add visual feedback
            completedEl.style.transform = 'scale(1.1)';
            setTimeout(() => {
                completedEl.style.transform = 'scale(1)';
            }, 200);
        } else {
            console.warn('completedDaysValue element not found');
        }

        if (totalEl) {
            console.log('Updating totalDaysValue from', totalEl.textContent, 'to', data.totalDays);
            totalEl.textContent = data.totalDays;
        } else {
            console.warn('totalDaysValue element not found');
        }

        if (remainingEl) {
            const remainingDays = data.totalDays - data.completedDays;
            console.log('Updating remainingDaysValue from', remainingEl.textContent, 'to', remainingDays);
            remainingEl.textContent = remainingDays;
            // Add visual feedback
            remainingEl.style.transform = 'scale(1.1)';
            setTimeout(() => {
                remainingEl.style.transform = 'scale(1)';
            }, 200);
        } else {
            console.warn('remainingDaysValue element not found');
        }

        // Update status alert immediately
        const statusAlert = document.getElementById('goalStatusAlert');
        if (statusAlert) {
            if (data.completed) {
                // Show success message for completed goals
                statusAlert.classList.remove('alert-info');
                statusAlert.classList.add('alert-success');
                statusAlert.innerHTML = '<i class="bi bi-check-circle"></i> 🎉 Congratulations!<br/>You\'ve achieved your goal!';
            } else {
                // Show progress message for ongoing goals
                statusAlert.classList.remove('alert-success');
                statusAlert.classList.add('alert-info');
                statusAlert.innerHTML = '<i class="bi bi-clock"></i> In Progress<br/>Keep going! You\'re doing great!';
            }
        }

        // Show confetti and celebration for completed goals
        if (data.completed) {
            createConfetti();
            showToast('🎉 Congratulations! Goal completed!', 'success');
        }

        console.log('Progress refresh completed successfully');

        // Log final values for verification
        console.log('Final values after update:');
        console.log('- Completed Days:', document.getElementById('completedDaysValue')?.textContent);
        console.log('- Remaining Days:', document.getElementById('remainingDaysValue')?.textContent);
        console.log('- Total Days:', document.getElementById('totalDaysValue')?.textContent);
        console.log('- Progress Text:', document.getElementById('progressDaysText')?.textContent);
        console.log('- Progress Percentage:', document.getElementById('progressPercentBadge')?.textContent);

    } catch (e) {
        console.error('Error refreshing progress:', e);
    }
}

/**
 * Animates a counter element from current value to target value
 * Creates a smooth counting animation effect
 * 
 * @param {HTMLElement} element - The element to animate
 * @param {string|number} targetValue - The final value to count to
 */
function animateCounter(element, targetValue) {
    // Handle string values (like "5/10 days") by extracting numbers
    let currentValue, target;

    if (typeof targetValue === 'string' && targetValue.includes('/')) {
        // Handle progress text like "5/10 days"
        const currentText = element.textContent;
        const currentMatch = currentText.match(/(\d+)\/(\d+)/);
        const targetMatch = targetValue.match(/(\d+)\/(\d+)/);

        if (currentMatch && targetMatch) {
            const currentCompleted = parseInt(currentMatch[1]);
            const currentTotal = parseInt(currentMatch[2]);
            const targetCompleted = parseInt(targetMatch[1]);
            const targetTotal = parseInt(targetMatch[2]);

            // Animate both numbers
            animateProgressText(element, currentCompleted, currentTotal, targetCompleted, targetTotal, targetValue);
            return;
        }
    }

    // Handle simple number values
    currentValue = parseInt(element.textContent) || 0;
    target = parseInt(targetValue) || 0;

    if (currentValue === target) {
        // No change needed
        return;
    }

    const increment = (target - currentValue) / 20; // Divide change into 20 steps
    let current = currentValue;

    // Create smooth counting animation
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= target) || (increment < 0 && current <= target)) {
            // Stop when we reach the target
            element.textContent = targetValue;
            clearInterval(timer);
        } else {
            // Show intermediate value
            element.textContent = Math.round(current);
        }
    }, 50); // Update every 50ms for smooth animation
}

/**
 * Animates progress text with two numbers (e.g., "5/10 days")
 * 
 * @param {HTMLElement} element - The element to animate
 * @param {number} currentCompleted - Current completed count
 * @param {number} currentTotal - Current total count
 * @param {number} targetCompleted - Target completed count
 * @param {number} targetTotal - Target total count
 * @param {string} finalText - Final text to display
 */
function animateProgressText(element, currentCompleted, currentTotal, targetCompleted, targetTotal, finalText) {
    const steps = 20;
    const completedIncrement = (targetCompleted - currentCompleted) / steps;
    const totalIncrement = (targetTotal - currentTotal) / steps;

    let currentComp = currentCompleted;
    let currentTot = currentTotal;
    let step = 0;

    const timer = setInterval(() => {
        step++;
        currentComp += completedIncrement;
        currentTot += totalIncrement;

        if (step >= steps) {
            // Final step - show exact target values
            element.textContent = finalText;
            clearInterval(timer);
        } else {
            // Show intermediate values
            const displayComp = Math.round(currentComp);
            const displayTot = Math.round(currentTot);
            element.textContent = `${displayComp}/${displayTot} days`;
        }
    }, 50);
}

/**
 * Converts a Date object to ISO date string format (YYYY-MM-DD)
 * Used for API calls and data attributes
 * 
 * @param {Date} d - The date to convert
 * @return {string} Date in YYYY-MM-DD format
 */
function toIsoDate(d) {
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0'); // Add leading zero if needed
    const day = String(d.getDate()).padStart(2, '0'); // Add leading zero if needed
    return `${year}-${month}-${day}`;
}

/**
 * Checks if two dates represent the same day
 * Compares year, month, and day (ignores time)
 * 
 * @param {Date} a - First date
 * @param {Date} b - Second date
 * @return {boolean} True if dates are the same day
 */
function sameDate(a, b) {
    return a.getFullYear() === b.getFullYear() &&
        a.getMonth() === b.getMonth() &&
        a.getDate() === b.getDate();
}

/**
 * Shows a message when user clicks on a future day
 * Informs user that the goal hasn't started yet
 * 
 * @param {Date} date - The future date that was clicked
 */
function showFutureDayMessage(date) {
    const formattedDate = date.toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
    showToast(`🎯 Goal starts on ${formattedDate} - Coming Soon!`, 'info');
}

// Enhanced Toast System

/**
 * Shows a toast notification to the user
 * Creates and displays a Bootstrap toast with custom styling
 * 
 * @param {string} message - The message to display
 * @param {string} type - The type of toast ('success', 'error', 'warning', 'info')
 */
function showToast(message, type = 'info') {
    // Get or create the toast container
    const toastContainer = document.getElementById('toastContainer') || createToastContainer();

    // Generate unique ID for this toast
    const toastId = 'toast-' + Date.now();

    // Create toast HTML with appropriate styling
    const toastHtml = `
        <div class="toast fade-in" id="${toastId}" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <i class="bi ${getToastIcon(type)} me-2"></i>
                <strong class="me-auto">${getToastTitle(type)}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${message}
            </div>
        </div>
    `;

    // Add toast to container
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);

    // Get the toast element and initialize Bootstrap toast
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, {
        autohide: true,
        delay: 4000 // Auto-hide after 4 seconds
    });

    // Show the toast
    toast.show();

    // Auto-remove toast element after animation completes
    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}

/**
 * Creates the toast container if it doesn't exist
 * The container holds all toast notifications
 * 
 * @return {HTMLElement} The toast container element
 */
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999'; // Ensure toasts appear above other elements
    document.body.appendChild(container);
    return container;
}

/**
 * Returns the appropriate Bootstrap icon class for toast type
 * 
 * @param {string} type - The toast type
 * @return {string} Bootstrap icon class
 */
function getToastIcon(type) {
    switch (type) {
        case 'success': return 'bi-check-circle-fill text-success';
        case 'error': return 'bi-exclamation-triangle-fill text-danger';
        case 'warning': return 'bi-exclamation-triangle-fill text-warning';
        case 'info': return 'bi-info-circle-fill text-info';
        default: return 'bi-info-circle-fill text-info';
    }
}

/**
 * Returns the appropriate title for toast type
 * 
 * @param {string} type - The toast type
 * @return {string} Toast title
 */
function getToastTitle(type) {
    switch (type) {
        case 'success': return 'Success!';
        case 'error': return 'Error!';
        case 'warning': return 'Warning!';
        case 'info': return 'Info';
        default: return 'Info';
    }
}

// Enhanced Confetti Animation

/**
 * Creates a confetti animation for goal completion celebration
 * Generates colorful confetti pieces that fall from the top of the screen
 */
function createConfetti() {
    // Define confetti colors for variety
    const colors = ['#0d6efd', '#198754', '#ffc107', '#dc3545', '#0dcaf0', '#6f42c1'];
    const confettiCount = 150; // Number of confetti pieces

    // Create confetti pieces with staggered timing
    for (let i = 0; i < confettiCount; i++) {
        setTimeout(() => {
            // Create confetti element
            const confetti = document.createElement('div');
            confetti.className = 'confetti';

            // Randomize confetti properties
            confetti.style.left = Math.random() * 100 + 'vw'; // Random horizontal position
            confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)]; // Random color
            confetti.style.width = (Math.random() * 10 + 5) + 'px'; // Random width
            confetti.style.height = (Math.random() * 10 + 5) + 'px'; // Random height
            confetti.style.animationDuration = (Math.random() * 3 + 2) + 's'; // Random animation duration
            confetti.style.animationDelay = Math.random() * 2 + 's'; // Random delay

            // Add confetti to page
            document.body.appendChild(confetti);

            // Remove confetti after animation completes
            setTimeout(() => {
                confetti.remove();
            }, 5000);
        }, i * 20); // Stagger creation by 20ms
    }
}

// Utility functions

/**
 * Formats a date string to a readable format
 * 
 * @param {string} dateString - Date string to format
 * @return {string} Formatted date string
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

/**
 * Formats a date string to a short format
 * 
 * @param {string} dateString - Date string to format
 * @return {string} Short formatted date string
 */
function formatShortDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric'
    });
}

// Export for use in other scripts (if using modules)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { initializeCalendar, createConfetti, showToast };
}


