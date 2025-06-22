package hr.tvz.trackerplatform.achievement.service;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.user.model.User;

import java.util.List;

public interface AchievementService {
    List<Achievement> checkAchievements();
    void generateAchievementsForUser(User user);
}
