package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckSubmitDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyQuestionDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.service.DailyCheckService;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyCheckServiceTest {

    @Mock
    private Mapper mapper;
    @Mock
    private DailyCheckRepository dailyCheckRepository;
    @Mock
    private UserSecurity userSecurity;

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

    @AfterEach
    void tearDown() {
        dailyCheck.setCompleted(false);
    }

    @Test
    void findAllCompletedCheckIns_shouldReturnEmptyList_whenNoneAreCompleted(){
        // Given
        User user = new User();
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(dailyCheckRepository.findAllByUser(user)).thenReturn(List.of(dailyCheck));
        when(mapper.mapList(Collections.emptyList(), DailyCheckDTO.class)).thenReturn(Collections.emptyList());

        // When
        List<DailyCheckDTO> allCompletedCheckIns = dailyCheckService.findAllCompletedCheckIns();

        // Then
        assertThat(allCompletedCheckIns)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void findAllCompletedCheckIns_shouldReturnACheckIn_whenItIsCompleted(){
        // Given
        User user = new User();
        dailyCheck.setCompleted(true);
        List<DailyCheck> dailyChecks = List.of(dailyCheck);
        List<DailyCheckDTO> dailyCheckDTOS = List.of(dailyCheckDTO);
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(dailyCheckRepository.findAllByUser(user)).thenReturn(dailyChecks);
        when(mapper.mapList(dailyChecks, DailyCheckDTO.class)).thenReturn(dailyCheckDTOS);

        // When
        List<DailyCheckDTO> allCompletedCheckIns = dailyCheckService.findAllCompletedCheckIns();

        // Then
        assertThat(allCompletedCheckIns)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(dailyCheckDTOS);;
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

    @Test
    void getDailyCheckByUuid_shouldThrowException_whenAlreadyCompleted() {
        UUID uuid = UUID.randomUUID();
        DailyCheck completedDailyCheck = DailyCheck.builder()
                .id(1L)
                .uuid(uuid)
                .checkInDate(LocalDate.now())
                .questions(List.of())
                .completed(true)
                .build();

        when(dailyCheckRepository.findByUuid(uuid)).thenReturn(Optional.of(completedDailyCheck));

        assertThatThrownBy(() -> dailyCheckService.getDailyCheckByUuid(uuid))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED.getMessage());
    }

    @Test
    void submitDailyCheck_shouldSubmitSuccessfully() {
        Long dailyCheckId = 1L;
        List<DailyQuestionDTO> questionDTOs = List.of(
                DailyQuestionDTO.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).score(5).build(),
                DailyQuestionDTO.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).score(4).build()
        );
        DailyCheckSubmitDTO submitDTO = DailyCheckSubmitDTO.builder()
                .id(dailyCheckId)
                .questions(questionDTOs)
                .build();

        List<DailyQuestion> mappedQuestions = List.of(
                DailyQuestion.builder().id(1L).content("Question 1").category(QuestionCategory.MENTAL).score(5).build(),
                DailyQuestion.builder().id(2L).content("Question 2").category(QuestionCategory.EMOTIONAL).score(4).build()
        );

        when(dailyCheckRepository.findById(dailyCheckId)).thenReturn(Optional.of(dailyCheck));
        when(mapper.mapList(questionDTOs, DailyQuestion.class)).thenReturn(mappedQuestions);

        dailyCheckService.submitDailyCheck(submitDTO);

        ArgumentCaptor<DailyCheck> dailyCheckCaptor = ArgumentCaptor.forClass(DailyCheck.class);
        verify(dailyCheckRepository).save(dailyCheckCaptor.capture());

        DailyCheck savedDailyCheck = dailyCheckCaptor.getValue();
        assertThat(savedDailyCheck.isCompleted()).isTrue();
        assertThat(savedDailyCheck.getCompletedAt()).isNotNull();
        assertThat(savedDailyCheck.getQuestions()).isEqualTo(mappedQuestions);
    }

    @Test
    void submitDailyCheck_shouldThrowException_whenDailyCheckNotFound() {
        Long dailyCheckId = 999L;
        DailyCheckSubmitDTO submitDTO = DailyCheckSubmitDTO.builder()
                .id(dailyCheckId)
                .questions(List.of())
                .build();

        when(dailyCheckRepository.findById(dailyCheckId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dailyCheckService.submitDailyCheck(submitDTO))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.DAILY_CHECK_NOT_FOUND.getMessage());
    }

    @Test
    void submitDailyCheck_shouldThrowException_whenAlreadySubmitted() {
        Long dailyCheckId = 1L;
        DailyCheckSubmitDTO submitDTO = DailyCheckSubmitDTO.builder()
                .id(dailyCheckId)
                .questions(List.of())
                .build();

        DailyCheck completedDailyCheck = DailyCheck.builder()
                .id(dailyCheckId)
                .uuid(UUID.randomUUID())
                .checkInDate(LocalDate.now())
                .questions(List.of())
                .completed(true)
                .completedAt(Instant.now())
                .build();

        when(dailyCheckRepository.findById(dailyCheckId)).thenReturn(Optional.of(completedDailyCheck));

        assertThatThrownBy(() -> dailyCheckService.submitDailyCheck(submitDTO))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED.getMessage());
    }
}
