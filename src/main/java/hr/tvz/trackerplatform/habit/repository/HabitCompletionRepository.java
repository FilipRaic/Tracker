package hr.tvz.trackerplatform.habit.repository;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {
    Optional<HabitCompletion> findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
            Habit habit, LocalDate completionDate);

    List<HabitCompletion> findAllByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
            Habit habit, LocalDate completionDate);

    Optional<HabitCompletion> findFirstByHabitAndDoneFalseAndCompletionDateBeforeOrderByCompletionDateDesc(
            Habit habit, LocalDate completionDate);

    Optional<HabitCompletion> findFirstByHabitAndDoneFalseAndCompletionDate(Habit habit, LocalDate completionDate);

    Optional<HabitCompletion> findFirstByHabitOrderByCompletionDateDesc(Habit habit);

    boolean existsByHabitAndCompletionDate(Habit habit, LocalDate completionDate);

    void deleteByHabit(Habit habit);
}
