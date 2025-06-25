package hr.tvz.trackerplatform.achievement.service;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.model.UserAchievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.achievement.repository.UserAchievementRepository;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.service.JournalEntryService;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private JournalEntryService journalEntryService;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Test
    void checkAchievements_shouldReturnWelcomeAndFirstPageAchievements_whenEntriesExist() {
        Achievement welcome = new Achievement();
        welcome.setName("Welcome");

        Achievement firstPage = new Achievement();
        firstPage.setName("First page");

        when(achievementRepository.findByName("Welcome")).thenReturn(Optional.of(welcome));
        when(achievementRepository.findByName("First page")).thenReturn(Optional.of(firstPage));
        when(journalEntryService.findAll()).thenReturn(List.of(new JournalEntryDTO()));

        List<Achievement> result = achievementService.checkAchievements();

        assertThat(result).containsExactly(welcome, firstPage);
    }

    @Test
    void checkAchievements_shouldReturnOnlyWelcome_whenNoJournalEntriesExist() {
        Achievement welcome = new Achievement();
        welcome.setName("Welcome");

        when(achievementRepository.findByName("Welcome")).thenReturn(Optional.of(welcome));
        when(journalEntryService.findAll()).thenReturn(List.of());

        List<Achievement> result = achievementService.checkAchievements();

        assertThat(result).containsExactly(welcome);
    }

    @Test
    void checkAchievements_shouldReturnEmpty_whenNoAchievementsPresent() {
        when(achievementRepository.findByName("Welcome")).thenReturn(Optional.empty());

        List<Achievement> result = achievementService.checkAchievements();

        assertThat(result).isEmpty();
    }

    @Test
    void generateAchievementsForUser_shouldCreateUserAchievementsWithCompletedFalse() {
        User user = new User();
        Achievement achievement1 = new Achievement();
        Achievement achievement2 = new Achievement();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));

        achievementService.generateAchievementsForUser(user);

        ArgumentCaptor<UserAchievement> captor = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository, times(2)).save(captor.capture());

        List<UserAchievement> savedAchievements = captor.getAllValues();
        assertThat(savedAchievements).hasSize(2)
                .allMatch(ua -> ua.getUser() == user && !ua.isCompleted())
                .extracting(UserAchievement::getAchievement)
                .containsExactlyInAnyOrder(achievement1, achievement2);
    }
}
