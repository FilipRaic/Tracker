package hr.tvz.trackerplatform.achievement.service;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.service.JournalEntryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AchievementService {

    AchievementRepository achievementRepository;
    JournalEntryService journalEntryService;
    public List<Achievement> checkAchievements(){
    List<Achievement> achievements=new ArrayList<>();
        if(checkWelcome()){
            achievements.add(achievementRepository.findByName("Welcome"));
        }
        if(checkFirstPage()){
            achievements.add(achievementRepository.findByName("First page"));
        }
        return achievements;
    }
    private Boolean checkWelcome(){
        return true;
    }
    private Boolean checkFirstPage(){
        List<JournalEntryDTO> journalEntryDTOS=journalEntryService.findAll();
        if (journalEntryDTOS.size()==0){
            return false;
        }
        else {
            return true;
        }
    }
}
