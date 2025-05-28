package hr.tvz.trackerplatform.repository;

import hr.tvz.trackerplatform.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
