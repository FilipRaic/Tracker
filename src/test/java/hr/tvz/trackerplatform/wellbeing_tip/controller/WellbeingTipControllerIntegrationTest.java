package hr.tvz.trackerplatform.wellbeing_tip.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WellbeingTipControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/tip";

    @Autowired
    private WellbeingTipRepository wellbeingTipRepository;
    @Autowired
    private DailyQuestionRepository dailyQuestionRepository;

    @Test
    void findWellbeingTips_shouldReturnTips_whenTipsExist() throws Exception {
        dailyQuestionRepository.saveAll(List.of(
                DailyQuestion.builder()
                        .content("Question 1 EN")
                        .contentDe("Question 1 DE")
                        .contentHr("Question 1 HR")
                        .category(QuestionCategory.PHYSICAL)
                        .score(2)
                        .build(),
                DailyQuestion.builder()
                        .content("Question 2 EN")
                        .contentDe("Question 2 DE")
                        .contentHr("Question 2 HR")
                        .category(QuestionCategory.MENTAL)
                        .score(4)
                        .build()
        ));

        wellbeingTipRepository.saveAll(List.of(
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(2)
                        .tipText("Drink more water")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(4)
                        .tipText("Practice mindfulness")
                        .build()
        ));

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
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
                                .tipText("Drink more water")
                                .build(),
                        WellbeingTipDTO.builder()
                                .category(QuestionCategory.MENTAL)
                                .score(4)
                                .tipText("Practice mindfulness")
                                .build()
                );
    }

    @Test
    void findWellbeingTips_shouldReturnEmptyList_whenNoTipsMatch() throws Exception {
        dailyQuestionRepository.save(
                DailyQuestion.builder()
                        .content("Question 2 EN")
                        .contentDe("Question 2 DE")
                        .contentHr("Question 2 HR")
                        .category(QuestionCategory.PHYSICAL)
                        .score(3)
                        .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<WellbeingTipDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEmpty();
    }

    @Test
    void findWellbeingTips_shouldReturnPartialList_whenSomeTipsMatch() throws Exception {
        dailyQuestionRepository.saveAll(List.of(
                DailyQuestion.builder()
                        .content("Question 2 EN")
                        .contentDe("Question 2 DE")
                        .contentHr("Question 2 HR")
                        .category(QuestionCategory.PHYSICAL)
                        .score(4)
                        .build(),
                DailyQuestion.builder()
                        .content("Question 2 EN")
                        .contentDe("Question 2 DE")
                        .contentHr("Question 2 HR")
                        .category(QuestionCategory.MENTAL)
                        .score(5)
                        .build()
        ));

        wellbeingTipRepository.save(
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(4)
                        .tipText("Drink more water")
                        .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
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
                                .tipText("Drink more water")
                                .build()
                );
    }

    @Test
    void findWellbeingTips_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }
}
