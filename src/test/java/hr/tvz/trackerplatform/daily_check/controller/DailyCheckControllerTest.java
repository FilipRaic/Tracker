package hr.tvz.trackerplatform.daily_check.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckSubmitDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyQuestionDTO;
import hr.tvz.trackerplatform.daily_check.service.DailyCheckService;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DailyCheckControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DailyCheckService dailyCheckService;

    @InjectMocks
    private DailyCheckController dailyCheckController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(dailyCheckController).build();
    }

    @Test
    void getCheckInByUuid_shouldReturnDailyCheck() throws Exception {
        UUID uuid = UUID.randomUUID();
        DailyCheckDTO dailyCheckDTO = DailyCheckDTO.builder()
                .id(1L)
                .questions(List.of(
                        DailyQuestionDTO.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).build(),
                        DailyQuestionDTO.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).build()
                ))
                .build();

        when(dailyCheckService.getDailyCheckByUuid(uuid)).thenReturn(dailyCheckDTO);

        mockMvc.perform(get("/api/daily-check/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.questions.length()").value(2))
                .andExpect(jsonPath("$.questions[0].content").value("Question 1"))
                .andExpect(jsonPath("$.questions[1].content").value("Question 2"));
    }

    @Test
    void submitDailyCheck_shouldSubmitSuccessfully() throws Exception {
        DailyCheckSubmitDTO submitDTO = DailyCheckSubmitDTO.builder()
                .id(1L)
                .questions(List.of(
                        DailyQuestionDTO.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).score(5).build(),
                        DailyQuestionDTO.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).score(4).build()
                ))
                .build();

        doNothing().when(dailyCheckService).submitDailyCheck(any(DailyCheckSubmitDTO.class));

        mockMvc.perform(post("/api/daily-check/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submitDTO)))
                .andExpect(status().isOk());

        verify(dailyCheckService).submitDailyCheck(any(DailyCheckSubmitDTO.class));
    }

    @Test
    void submitDailyCheck_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        DailyCheckSubmitDTO invalidSubmitDTO = DailyCheckSubmitDTO.builder()
                .id(null)
                .questions(List.of())
                .build();

        mockMvc.perform(post("/api/daily-check/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSubmitDTO)))
                .andExpect(status().isBadRequest());

        verify(dailyCheckService, never()).submitDailyCheck(any(DailyCheckSubmitDTO.class));
    }
}
