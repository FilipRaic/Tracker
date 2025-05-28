package hr.tvz.trackerplatform.repository;

import hr.tvz.trackerplatform.model.HabitFrequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitFrequencyRepository extends JpaRepository<HabitFrequency, Integer> {
    Optional<HabitFrequency> findByName(String name);
}
