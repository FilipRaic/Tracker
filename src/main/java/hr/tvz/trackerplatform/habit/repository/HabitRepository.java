package hr.tvz.trackerplatform.habit.repository;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUser(User user);
}
