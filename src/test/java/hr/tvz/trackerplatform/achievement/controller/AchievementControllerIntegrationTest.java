package hr.tvz.trackerplatform.achievement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AchievementControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/achievement";

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Test
    void findAllAchievements_shouldReturnAchievementsList() throws Exception {
        Achievement achievement1 = Achievement.builder()
                .name("Welcome")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement achievement2 = Achievement.builder()
                .name("First page")
                .unlockCondition("Run 10km in a week")
                .emoji("🏃")
                .description("Awarded for running 10 kilometers in a single week.")
                .build();

        achievement1 = achievementRepository.save(achievement1);
        achievementRepository.save(achievement2);

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Achievement> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .singleElement()
                .isEqualTo(Achievement.builder()
                        .id(achievement1.getId())
                        .name("Welcome")
                        .unlockCondition("Complete 5 daily checks")
                        .emoji("🏆")
                        .description("Awarded for completing your first 5 daily checks.")
                        .build()
                );
    }

    @Test
    void findAllAchievementsAndHasJournalEntries_shouldReturnAchievementsList() throws Exception {
        journalEntryRepository.save(JournalEntry.builder()
                .description("Test description")
                .date(LocalDate.now())
                .user(user)
                .build());

        Achievement achievement1 = achievementRepository.save(Achievement.builder()
                .name("Welcome")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build());

        Achievement achievement2 = achievementRepository.save(Achievement.builder()
                .name("First page")
                .unlockCondition("Run 10km in a week")
                .emoji("🏃")
                .description("Awarded for running 10 kilometers in a single week.")
                .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Achievement> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .hasSize(2)
                .isEqualTo(List.of(
                                Achievement.builder()
                                        .id(achievement1.getId())
                                        .name("Welcome")
                                        .unlockCondition("Complete 5 daily checks")
                                        .emoji("🏆")
                                        .description("Awarded for completing your first 5 daily checks.")
                                        .build(),
                                Achievement.builder()
                                        .id(achievement2.getId())
                                        .name("First page")
                                        .unlockCondition("Run 10km in a week")
                                        .emoji("🏃")
                                        .description("Awarded for running 10 kilometers in a single week.")
                                        .build()
                        )
                );
    }

    @Test
    void findAllAchievements_shouldReturnEmptyList_whenNoAchievements() throws Exception {
        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Achievement> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEmpty();
    }
}
