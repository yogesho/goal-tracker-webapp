package com.goaltracker.repository;

import com.goaltracker.entity.Goal;
import com.goaltracker.entity.GoalDay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoalDayRepositoryTest {

    @Autowired
    private GoalDayRepository goalDayRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void deleteByGoalId_shouldRemoveAllDays() {
        Goal g = new Goal("Valid Title", "B desc", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3));
        g = goalRepository.save(g);

        goalDayRepository.save(new GoalDay(g, LocalDate.of(2024, 1, 1)));
        goalDayRepository.save(new GoalDay(g, LocalDate.of(2024, 1, 2)));
        goalDayRepository.save(new GoalDay(g, LocalDate.of(2024, 1, 3)));

        assertEquals(3, goalDayRepository.countTotalDaysByGoalId(g.getId()));

        goalDayRepository.deleteByGoalId(g.getId());

        assertEquals(0, goalDayRepository.countTotalDaysByGoalId(g.getId()));
    }
}
