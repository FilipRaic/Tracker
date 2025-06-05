package hr.tvz.trackerplatform.habit.repository;

import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitFrequencyRepository extends JpaRepository<HabitFrequency, Integer> {

    Optional<HabitFrequency> findByName(String name);
}
