package hr.tvz.trackerplatform.repository;

import hr.tvz.trackerplatform.model.Habit;
import hr.tvz.trackerplatform.model.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {
    Optional<HabitCompletion> findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
            Habit habit, LocalDate completionDate);

    boolean existsByHabitAndCompletionDate(Habit habit, LocalDate completionDate);
}
