package hr.tvz.trackerplatform.achievement.service;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.model.UserAchievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.achievement.repository.UserAchievementRepository;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.service.JournalEntryService;
import hr.tvz.trackerplatform.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final JournalEntryService journalEntryService;

    @Override
    public List<Achievement> checkAchievements() {
        List<Achievement> achievements = new ArrayList<>();

        Optional<Achievement> welcomeAchievement = achievementRepository.findByName("Welcome");
        welcomeAchievement.ifPresent(achievements::add);

        if (checkFirstPage()) {
            Optional<Achievement> firstPageAchievement = achievementRepository.findByName("First page");
            firstPageAchievement.ifPresent(achievements::add);
        }

        return achievements;
    }

    @Override
    public void generateAchievementsForUser(User user) {
        List<Achievement> achievements = achievementRepository.findAll();
        for (Achievement achievement : achievements) {
            UserAchievement userAchievement = buildUserAchievement(user, achievement);
            userAchievementRepository.save(userAchievement);
        }
    }

    private boolean checkFirstPage() {
        List<JournalEntryDTO> journalEntryDTOS = journalEntryService.findAll();

        return !journalEntryDTOS.isEmpty();
    }

    private UserAchievement buildUserAchievement(User user, Achievement achievement) {
        return UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(false)
                .build();
    }
}
