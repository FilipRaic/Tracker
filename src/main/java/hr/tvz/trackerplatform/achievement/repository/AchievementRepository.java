package hr.tvz.trackerplatform.achievement.repository;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement,Long> {
    Achievement findByName(String name);
}
