package hr.tvz.trackerplatform.daily_check.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckSubmitDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyQuestionDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyCheckControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/daily-check";
    private static final String BASE_URL_PUBLIC = "/api/daily-check/public";

    @Autowired
    private DailyCheckRepository dailyCheckRepository;

    @Test
    void findAllCompletedCheckIns_shouldReturnCompletedCheckIns() throws Exception {
        DailyCheck dailyCheck = DailyCheck.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .checkInDate(LocalDate.now())
                .completed(true)
                .completedAt(Instant.now())
                .questions(List.of(
                        DailyQuestion.builder()
                                .content("Question 1 EN")
                                .contentDe("Question 1 DE")
                                .contentHr("Question 1 HR")
                                .category(QuestionCategory.MENTAL)
                                .score(5)
                                .build(),
                        DailyQuestion.builder()
                                .content("Question 2 EN")
                                .contentDe("Question 2 DE")
                                .contentHr("Question 2 HR")
                                .category(QuestionCategory.EMOTIONAL)
                                .score(4)
                                .build()
                ))
                .build();

        dailyCheck = dailyCheckRepository.save(dailyCheck);

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/completed")))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<DailyCheckDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .singleElement()
                .usingRecursiveComparison()
                .ignoringFields("questions.id")
                .isEqualTo(DailyCheckDTO.builder()
                        .id(dailyCheck.getId())
                        .questions(List.of(
                                DailyQuestionDTO.builder()
                                        .category(QuestionCategory.MENTAL)
                                        .content("Question 1 EN")
                                        .contentDe("Question 1 DE")
                                        .contentHr("Question 1 HR")
                                        .score(5)
                                        .build(),
                                DailyQuestionDTO.builder()
                                        .category(QuestionCategory.EMOTIONAL)
                                        .content("Question 2 EN")
                                        .contentDe("Question 2 DE")
                                        .contentHr("Question 2 HR")
                                        .score(4)
                                        .build()
                        ))
                        .completed(true)
                        .checkInDate(LocalDate.now())
                        .userFirstName(user.getFirstName())
                        .build());
    }

    @Test
    void findAllCompletedCheckIns_shouldReturnNoCheckIns() throws Exception {
        DailyCheck dailyCheck = DailyCheck.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .checkInDate(LocalDate.now())
                .completed(false)
                .completedAt(null)
                .questions(List.of(
                        DailyQuestion.builder()
                                .content("Question 1 EN")
                                .contentDe("Question 1 DE")
                                .contentHr("Question 1 HR")
                                .category(QuestionCategory.MENTAL)
                                .score(null)
                                .build(),
                        DailyQuestion.builder()
                                .content("Question 2 EN")
                                .contentDe("Question 2 DE")
                                .contentHr("Question 2 HR")
                                .category(QuestionCategory.EMOTIONAL)
                                .score(null)
                                .build()
                ))
                .build();
        dailyCheckRepository.save(dailyCheck);

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/completed")))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<DailyCheckDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEmpty();
    }

    @Test
    void getCheckInByUuid_shouldReturnDailyCheck() throws Exception {
        UUID dailyCheckUuid = UUID.randomUUID();

        DailyCheck dailyCheck = DailyCheck.builder()
                .uuid(dailyCheckUuid)
                .user(user)
                .checkInDate(LocalDate.now())
                .completed(false)
                .completedAt(null)
                .questions(List.of(
                        DailyQuestion.builder()
                                .content("Question 1 EN")
                                .contentDe("Question 1 DE")
                                .contentHr("Question 1 HR")
                                .category(QuestionCategory.MENTAL)
                                .score(null)
                                .build(),
                        DailyQuestion.builder()
                                .content("Question 2 EN")
                                .contentDe("Question 2 DE")
                                .contentHr("Question 2 HR")
                                .category(QuestionCategory.EMOTIONAL)
                                .score(null)
                                .build()
                ))
                .build();

        dailyCheck = dailyCheckRepository.save(dailyCheck);

        var response = mockMvc.perform(get(BASE_URL_PUBLIC + "/" + dailyCheckUuid))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        DailyCheckDTO actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("questions.id")
                .isEqualTo(DailyCheckDTO.builder()
                        .id(dailyCheck.getId())
                        .questions(List.of(
                                DailyQuestionDTO.builder()
                                        .category(QuestionCategory.MENTAL)
                                        .content("Question 1 EN")
                                        .contentDe("Question 1 DE")
                                        .contentHr("Question 1 HR")
                                        .score(null)
                                        .build(),
                                DailyQuestionDTO.builder()
                                        .category(QuestionCategory.EMOTIONAL)
                                        .content("Question 2 EN")
                                        .contentDe("Question 2 DE")
                                        .contentHr("Question 2 HR")
                                        .score(null)
                                        .build()
                        ))
                        .completed(false)
                        .checkInDate(LocalDate.now())
                        .userFirstName(user.getFirstName())
                        .build());
    }

    @Test
    void submitDailyCheck_shouldSubmitSuccessfully() throws Exception {
        UUID dailyCheckUuid = UUID.randomUUID();

        DailyCheck dailyCheck = DailyCheck.builder()
                .uuid(dailyCheckUuid)
                .user(user)
                .checkInDate(LocalDate.now())
                .completed(false)
                .completedAt(null)
                .questions(List.of(
                        DailyQuestion.builder()
                                .content("Question 1 EN")
                                .contentDe("Question 1 DE")
                                .contentHr("Question 1 HR")
                                .category(QuestionCategory.MENTAL)
                                .score(null)
                                .build(),
                        DailyQuestion.builder()
                                .content("Question 2 EN")
                                .contentDe("Question 2 DE")
                                .contentHr("Question 2 HR")
                                .category(QuestionCategory.EMOTIONAL)
                                .score(null)
                                .build()
                ))
                .build();

        dailyCheck = dailyCheckRepository.save(dailyCheck);

        DailyCheckSubmitDTO submitDTO = DailyCheckSubmitDTO.builder()
                .id(dailyCheck.getId())
                .questions(List.of(
                        DailyQuestionDTO.builder()
                                .content("Question 1 EN")
                                .contentDe("Question 1 DE")
                                .contentHr("Question 1 HR")
                                .category(QuestionCategory.MENTAL)
                                .score(4)
                                .build(),
                        DailyQuestionDTO.builder()
                                .content("Question 2 EN")
                                .contentDe("Question 2 DE")
                                .contentHr("Question 2 HR")
                                .category(QuestionCategory.EMOTIONAL)
                                .score(5)
                                .build()
                ))
                .build();

        mockMvc.perform(withJwt(post(BASE_URL_PUBLIC + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(submitDTO))))
                .andExpect(status().isOk());

        DailyCheck actual = dailyCheckRepository.findById(dailyCheck.getId()).orElseThrow();

        assertThat(actual).satisfies(check -> {
                    assertThat(check.isCompleted()).isTrue();
                    assertThat(check.getCompletedAt()).isNotNull();
                }
        );
    }

    @Test
    void submitDailyCheck_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        DailyCheckSubmitDTO invalidSubmitDTO = DailyCheckSubmitDTO.builder()
                .id(null)
                .questions(List.of())
                .build();

        mockMvc.perform(withJwt(post(BASE_URL_PUBLIC + "/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidSubmitDTO))))
                .andExpect(status().isBadRequest());
    }
}
