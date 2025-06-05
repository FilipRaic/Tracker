package hr.tvz.trackerplatform.habit.repository;

import hr.tvz.trackerplatform.habit.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
}
