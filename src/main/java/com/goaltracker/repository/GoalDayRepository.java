package com.goaltracker.repository;

import com.goaltracker.entity.GoalDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalDayRepository extends JpaRepository<GoalDay, Long> {

    @Query("SELECT gd FROM GoalDay gd WHERE gd.goal.id = :goalId AND gd.date = :date")
    Optional<GoalDay> findByGoalIdAndDate(@Param("goalId") Long goalId, @Param("date") LocalDate date);

    @Query("SELECT gd FROM GoalDay gd WHERE gd.goal.id = :goalId ORDER BY gd.date ASC")
    List<GoalDay> findByGoalIdOrderByDayDate(@Param("goalId") Long goalId);

    @Query("SELECT COUNT(gd) FROM GoalDay gd WHERE gd.goal.id = :goalId AND gd.completed = true")
    long countCompletedDaysByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT COUNT(gd) FROM GoalDay gd WHERE gd.goal.id = :goalId")
    long countTotalDaysByGoalId(@Param("goalId") Long goalId);

    @Modifying
    @Query("DELETE FROM GoalDay gd WHERE gd.goal.id = :goalId")
    void deleteByGoalId(@Param("goalId") Long goalId);
}
