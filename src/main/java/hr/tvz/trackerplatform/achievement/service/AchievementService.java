package hr.tvz.trackerplatform.achievement.service;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.service.JournalEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final JournalEntryService journalEntryService;

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

    private boolean checkFirstPage() {
        List<JournalEntryDTO> journalEntryDTOS = journalEntryService.findAll();

        return !journalEntryDTOS.isEmpty();
    }
}
