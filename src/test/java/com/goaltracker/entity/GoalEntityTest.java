package com.goaltracker.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GoalEntityTest {
    @Test
    void totalDays_inclusiveRange() {
        Goal g = new Goal("A", "B", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));
        assertEquals(3, g.getTotalDays());
    }

    @Test
    void progress_zeroWhenNoDays() {
        Goal g = new Goal("A", "B", null, null);
        assertEquals(0.0, g.getProgressPercentage());
    }
}
