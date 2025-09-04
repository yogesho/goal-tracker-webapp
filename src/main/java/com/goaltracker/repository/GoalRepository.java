package com.goaltracker.repository;

import com.goaltracker.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId ORDER BY g.createdAt DESC")
    List<Goal> findAllByUserIdOrderByCreatedAtDesc(
            @org.springframework.data.repository.query.Param("userId") Long userId);

    @Query("SELECT g FROM Goal g ORDER BY g.createdAt DESC")
    List<Goal> findAllOrderByCreatedAtDesc();

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.endDate >= CURRENT_DATE ORDER BY g.endDate ASC")
    List<Goal> findActiveGoalsByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @Query("SELECT g FROM Goal g WHERE g.endDate >= CURRENT_DATE ORDER BY g.endDate ASC")
    List<Goal> findActiveGoals();

    @Query("SELECT g FROM Goal g WHERE g.user.id = :userId AND g.endDate < CURRENT_DATE ORDER BY g.endDate DESC")
    List<Goal> findCompletedGoalsByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @Query("SELECT g FROM Goal g WHERE g.endDate < CURRENT_DATE ORDER BY g.endDate DESC")
    List<Goal> findCompletedGoals();
}
