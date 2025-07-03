package hr.tvz.trackerplatform.wellbeing_tip.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WellbeingTipControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/tip";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DailyCheckRepository dailyCheckRepository;
    @Autowired
    private WellbeingTipRepository wellbeingTipRepository;
    @Autowired
    private DailyQuestionRepository dailyQuestionRepository;


    @Test
    void findWellbeingTips_shouldReturnTips_whenTipsExist() throws Exception {
        User user = userRepository.save(User.builder()
                .email("test2@test.com")
                .role(Role.USER)
                .lastName("last")
                .firstName("first")
                .password("password")
                .build());

        DailyQuestion dailyQuestion1 = DailyQuestion.builder()
                .content("Question 1 EN")
                .contentDe("Question 1 DE")
                .contentHr("Question 1 HR")
                .category(QuestionCategory.PHYSICAL)
                .score(2)
                .build();

        DailyQuestion dailyQuestion2 = DailyQuestion.builder()
                .content("Question 2 EN")
                .contentDe("Question 2 DE")
                .contentHr("Question 2 HR")
                .category(QuestionCategory.MENTAL)
                .score(4)
                .build();

        wellbeingTipRepository.saveAll(List.of(
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(2)
                        .tipTextEn("Drink more water")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(4)
                        .tipTextEn("Practice mindfulness")
                        .build()
        ));

        dailyCheckRepository.save(
                DailyCheck.builder()
                        .user(user)
                        .questions(List.of(dailyQuestion1, dailyQuestion2))
                        .completed(true)
                        .completedAt(Instant.now())
                        .checkInDate(LocalDate.now())
                        .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<WellbeingTipDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        WellbeingTipDTO.builder()
                                .category(QuestionCategory.PHYSICAL)
                                .score(2)
                                .tipTextEn("Drink more water")
                                .build(),
                        WellbeingTipDTO.builder()
                                .category(QuestionCategory.MENTAL)
                                .score(4)
                                .tipTextEn("Practice mindfulness")
                                .build()
                );
    }

    @Test
    void findWellbeingTips_shouldReturnEmptyList_whenNoTipsMatch() throws Exception {
        User user = userRepository.save(User.builder()
                .email("test4@test.com")
                .role(Role.USER)
                .lastName("last")
                .firstName("first")
                .password("password")
                .build());

        DailyQuestion dailyQuestion = DailyQuestion.builder()
                .content("Question 2 EN")
                .contentDe("Question 2 DE")
                .contentHr("Question 2 HR")
                .category(QuestionCategory.PHYSICAL)
                .score(3)
                .build();

        dailyCheckRepository.save(
                DailyCheck.builder()
                        .user(user)
                        .questions(List.of(dailyQuestion))
                        .completed(true)
                        .completedAt(Instant.now())
                        .checkInDate(LocalDate.now())
                        .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<WellbeingTipDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEmpty();
    }

    @Test
    void findWellbeingTips_shouldReturnPartialList_whenSomeTipsMatch() throws Exception {
        User user = userRepository.save(User.builder().email("test3@test.com").role(Role.USER).lastName("asd").firstName("asd")
                .password("password").build());
        DailyQuestion dailyQuestion1 = DailyQuestion.builder()
                .content("Question 2 EN")
                .contentDe("Question 2 DE")
                .contentHr("Question 2 HR")
                .category(QuestionCategory.PHYSICAL)
                .score(4)
                .build();
        DailyQuestion dailyQuestion2 = DailyQuestion.builder()
                .content("Question 2 EN")
                .contentDe("Question 2 DE")
                .contentHr("Question 2 HR")
                .category(QuestionCategory.MENTAL)
                .score(5)
                .build();

        wellbeingTipRepository.save(
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(4)
                        .tipTextEn("Drink more water")
                        .build());

        dailyCheckRepository.save(
                DailyCheck.builder()
                        .user(user)
                        .questions(List.of(dailyQuestion1, dailyQuestion2))
                        .completed(true)
                        .completedAt(Instant.now())
                        .checkInDate(LocalDate.now())
                        .build());


        var response = mockMvc.perform(withJwt(get(BASE_URL + "/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<WellbeingTipDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .hasSize(1)
                .containsExactly(
                        WellbeingTipDTO.builder()
                                .category(QuestionCategory.PHYSICAL)
                                .score(4)
                                .tipTextEn("Drink more water")
                                .build()
                );
    }

    @Test
    void findWellbeingTips_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void calculateStreak_shouldReturnStreakOf3_whenConsecutiveDaysCompleted() throws Exception {
        User user = userRepository.save(User.builder().email("test3@test.com").role(Role.USER).lastName("asd").firstName("asd")
                .password("password").build());

        dailyCheckRepository.saveAll(List.of(
                DailyCheck.builder().user(user).checkInDate(LocalDate.now()).completed(true).build(),
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(1)).completed(true).build(),
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(2)).completed(true).build()
        ));

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/streak/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Integer actual = mapper.readValue(response.getContentAsString(), Integer.class);

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void calculateStreak_shouldReturn1_whenYesterdayUncompleted() throws Exception {
        User user = userRepository.save(User.builder().email("test3@test.com").role(Role.USER).lastName("asd").firstName("asd")
                .password("password").build());

        dailyCheckRepository.saveAll(List.of(
                DailyCheck.builder().user(user).checkInDate(LocalDate.now()).completed(true).build(),
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(1)).completed(false).build(),
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(2)).completed(true).build()
        ));

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/streak/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Integer actual = mapper.readValue(response.getContentAsString(), Integer.class);

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void calculateStreak_shouldReturnZero_whenNoChecksToday() throws Exception {
        User user = userRepository.save(User.builder().email("test3@test.com").role(Role.USER).lastName("asd").firstName("asd")
                .password("password").build());
        userRepository.flush();
        dailyCheckRepository.saveAll(List.of(
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(1)).completed(true).build(),
                DailyCheck.builder().user(user).checkInDate(LocalDate.now().minusDays(2)).completed(true).build()
        ));

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/streak/" + user.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Integer actual = mapper.readValue(response.getContentAsString(), Integer.class);

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void calculateStreak_shouldReturnZero_whenUserNotFound() throws Exception {
        var response = mockMvc.perform(withJwt(get(BASE_URL + "/streak/999")))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Integer actual = mapper.readValue(response.getContentAsString(), Integer.class);

        assertThat(actual).isZero();
    }
}
