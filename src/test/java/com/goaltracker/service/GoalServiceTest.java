package com.goaltracker.service;

import com.goaltracker.dto.GoalDto;
import com.goaltracker.entity.Goal;
import com.goaltracker.entity.GoalDay;
import com.goaltracker.repository.GoalDayRepository;
import com.goaltracker.repository.GoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalDayRepository goalDayRepository;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGoal_shouldPersistGoalAndDays() {
        GoalDto input = new GoalDto("Test", "Desc", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));
        Goal saved = new Goal("Test", "Desc", input.getStartDate(), input.getEndDate());
        saved.setId(10L);
        when(goalRepository.save(any(Goal.class))).thenReturn(saved);
        when(goalDayRepository.save(any(GoalDay.class))).thenAnswer(inv -> inv.getArgument(0));

        GoalDto result = goalService.createGoal(input);

        assertNotNull(result.getId());
        verify(goalDayRepository, atLeast(3)).save(any(GoalDay.class));
    }

    @Test
    void toggleGoalDay_shouldCreateIfMissingAndToggle() {
        Goal g = new Goal("T", "D", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        g.setId(5L);
        when(goalRepository.findById(5L)).thenReturn(Optional.of(g));
        when(goalDayRepository.findByGoalIdAndDate(eq(5L), any(LocalDate.class))).thenReturn(Optional.empty());
        when(goalDayRepository.save(any(GoalDay.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean afterToggle = goalService.toggleGoalDay(5L, LocalDate.of(2024, 1, 10));
        assertTrue(afterToggle);
        verify(goalDayRepository, times(2)).save(any(GoalDay.class));
    }

    @Test
    void updateGoal_shouldResetDays() {
        Goal existing = new Goal("Aaa", "Bbb", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5));
        existing.setId(2L);
        when(goalRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(goalRepository.save(any(Goal.class))).thenReturn(existing);
        when(goalDayRepository.save(any(GoalDay.class))).thenAnswer(inv -> inv.getArgument(0));

        GoalDto update = new GoalDto("A2", "B2", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 3));
        GoalDto out = goalService.updateGoal(2L, update);

        assertEquals("A2", out.getTitle());
        verify(goalDayRepository, times(1)).deleteByGoalId(2L);
        verify(goalDayRepository, atLeast(3)).save(any(GoalDay.class));
    }
}
