package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyQuestionDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.service.DailyCheckService;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceTest {

    @Mock
    private Mapper mapper;
    @Mock
    private DailyCheckRepository dailyCheckRepository;

    @InjectMocks
    private DailyCheckService dailyCheckService;

    private DailyCheck dailyCheck;
    private DailyCheckDTO dailyCheckDTO;

    @BeforeEach
    void setUp() {
        List<DailyQuestion> dailyQuestions = List.of(
                DailyQuestion.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).build(),
                DailyQuestion.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).build(),
                DailyQuestion.builder().id(3L).content("Question 3").category(QuestionCategory.PHYSICAL).build(),
                DailyQuestion.builder().id(4L).content("Question 4").category(QuestionCategory.SOCIAL).build()
        );

        List<DailyQuestionDTO> dailyQuestionDtos = List.of(
                DailyQuestionDTO.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).build(),
                DailyQuestionDTO.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).build(),
                DailyQuestionDTO.builder().id(3L).content("Question 3").category(QuestionCategory.PHYSICAL).build(),
                DailyQuestionDTO.builder().id(4L).content("Question 4").category(QuestionCategory.SOCIAL).build()
        );

        dailyCheck = DailyCheck.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .checkInDate(LocalDate.now())
                .questions(dailyQuestions)
                .completed(false)
                .build();

        dailyCheckDTO = DailyCheckDTO.builder()
                .id(1L)
                .questions(dailyQuestionDtos)
                .build();
    }

    @Test
    void getDailyCheckByUuid_shouldReturnCheckIn_whenExists() {
        UUID uuid = UUID.randomUUID();

        when(dailyCheckRepository.findByUuid(uuid)).thenReturn(Optional.of(dailyCheck));
        when(mapper.map(dailyCheck, DailyCheckDTO.class)).thenReturn(dailyCheckDTO);

        DailyCheckDTO actual = dailyCheckService.getDailyCheckByUuid(uuid);

        assertThat(actual).isNotNull()
                .isEqualTo(dailyCheckDTO);
    }

    @Test
    void getDailyCheckByUuid_shouldReturnEmpty_whenNotExists() {
        UUID uuid = UUID.randomUUID();
        when(dailyCheckRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dailyCheckService.getDailyCheckByUuid(uuid))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.DAILY_CHECK_NOT_FOUND.getMessage());
    }
}
