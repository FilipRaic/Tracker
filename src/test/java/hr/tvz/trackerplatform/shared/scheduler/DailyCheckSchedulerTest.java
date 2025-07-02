package hr.tvz.trackerplatform.shared.scheduler;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.question.model.Question;
import hr.tvz.trackerplatform.question.repository.QuestionRepository;
import hr.tvz.trackerplatform.shared.service.EmailServiceImpl;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyCheckSchedulerTest {

    private static User user;

    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private DailyCheckRepository dailyCheckRepository;

    @InjectMocks
    private DailyCheckScheduler dailyCheckScheduler;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Davis")
                .lastName("Davis")
                .email("davis@mail.com")
                .role(Role.USER)
                .build();

        when(userRepository.findAllWithoutDailyCheckForDate(LocalDate.now())).thenReturn(List.of(user));
    }

    @Test
    void scheduleDailyChecks_shouldGenerateAndSendDailyCheck() {
        LocalDate today = LocalDate.now();

        when(dailyCheckRepository.existsByCheckInDateAndUser(today, user)).thenReturn(false);

        List<Question> randomQuestions = List.of(
                Question.builder().id(1L).content("Question 1").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.MENTAL).build(),
                Question.builder().id(2L).content("Question 2").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.EMOTIONAL).build(),
                Question.builder().id(3L).content("Question 3").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.PHYSICAL).build(),
                Question.builder().id(4L).content("Question 4").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.SOCIAL).build()
        );
        when(questionRepository.findRandomActiveQuestions(4)).thenReturn(randomQuestions);

        DailyCheck savedDailyCheck = DailyCheck.builder()
                .id(1L)
                .checkInDate(today)
                .questions(List.of())
                .completed(false)
                .build();
        when(dailyCheckRepository.saveAndFlush(any(DailyCheck.class))).thenReturn(savedDailyCheck);

        dailyCheckScheduler.scheduleDailyChecks();

        ArgumentCaptor<DailyCheck> dailyCheckCaptor = ArgumentCaptor.forClass(DailyCheck.class);
        verify(dailyCheckRepository).saveAndFlush(dailyCheckCaptor.capture());
        verify(emailService).sendDailyCheckEmail(savedDailyCheck, user);

        DailyCheck capturedDailyCheck = dailyCheckCaptor.getValue();
        assertThat(capturedDailyCheck.getCheckInDate()).isEqualTo(today);
        assertThat(capturedDailyCheck.isCompleted()).isFalse();
        assertThat(capturedDailyCheck.getQuestions()).hasSize(4);
    }

    @Test
    void scheduleDailyChecks_shouldNotGenerateDailyCheck_whenAlreadyExistsForToday() {
        LocalDate today = LocalDate.now();
        when(dailyCheckRepository.existsByCheckInDateAndUser(today, user)).thenReturn(true);

        dailyCheckScheduler.scheduleDailyChecks();

        verify(questionRepository, never()).findRandomActiveQuestions(anyInt());
        verify(dailyCheckRepository, never()).saveAndFlush(any(DailyCheck.class));
        verify(emailService, never()).sendDailyCheckEmail(any(DailyCheck.class), eq(user));
    }

    @Test
    void scheduleDailyChecks_shouldNotGenerateDailyCheck_whenNotEnoughQuestionsAvailable() {
        LocalDate today = LocalDate.now();
        when(dailyCheckRepository.existsByCheckInDateAndUser(today, user)).thenReturn(false);

        List<Question> randomQuestions = List.of(
                Question.builder().id(1L).content("Question 1").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.MENTAL).build(),
                Question.builder().id(2L).content("Question 2").category(hr.tvz.trackerplatform.question.enums.QuestionCategory.EMOTIONAL).build()
        );
        when(questionRepository.findRandomActiveQuestions(4)).thenReturn(randomQuestions);

        dailyCheckScheduler.scheduleDailyChecks();

        verify(dailyCheckRepository, never()).saveAndFlush(any(DailyCheck.class));
        verify(emailService, never()).sendDailyCheckEmail(any(DailyCheck.class), eq(user));
    }

    @Test
    void scheduleDailyChecks_shouldHandleExceptions() {
        LocalDate today = LocalDate.now();
        when(dailyCheckRepository.existsByCheckInDateAndUser(today, user)).thenReturn(false);
        when(questionRepository.findRandomActiveQuestions(4)).thenThrow(new RuntimeException("Test exception"));

        dailyCheckScheduler.scheduleDailyChecks();

        verify(dailyCheckRepository, never()).saveAndFlush(any(DailyCheck.class));
        verify(emailService, never()).sendDailyCheckEmail(any(DailyCheck.class), eq(user));
    }
}
