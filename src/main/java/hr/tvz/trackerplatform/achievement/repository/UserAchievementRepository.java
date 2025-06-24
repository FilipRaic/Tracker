package hr.tvz.trackerplatform.achievement.repository;

import hr.tvz.trackerplatform.achievement.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
}
