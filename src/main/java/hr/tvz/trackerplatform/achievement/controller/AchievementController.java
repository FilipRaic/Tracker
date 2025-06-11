package hr.tvz.trackerplatform.achievement.controller;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.service.AchievementService;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/achievement")
public class AchievementController {
    AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<Achievement>> findAllAchievements() {
        List<Achievement> achievements = achievementService.checkAchievements();
        return ResponseEntity.ok(achievements);
    }
}
