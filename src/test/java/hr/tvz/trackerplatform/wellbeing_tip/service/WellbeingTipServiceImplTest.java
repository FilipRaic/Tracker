package hr.tvz.trackerplatform.wellbeing_tip.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WellbeingTipServiceImplTest {

    @Mock
    private Mapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DailyCheckRepository dailyCheckRepository;
    @Mock
    private WellbeingTipRepository wellbeingTipRepository;

    @InjectMocks
    private WellbeingTipServiceImpl wellbeingTipService;

    private DailyQuestion dailyQuestion1;
    private DailyQuestion dailyQuestion2;
    private WellbeingTip wellbeingTip1;
    private WellbeingTip wellbeingTip2;
    private WellbeingTipDTO wellbeingTipDTO1;
    private WellbeingTipDTO wellbeingTipDTO2;
    private User user;
    private List<DailyCheck> dailyChecks;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();
        dailyQuestion1 = DailyQuestion.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .build();

        dailyQuestion2 = DailyQuestion.builder()
                .id(2L)
                .category(QuestionCategory.MENTAL)
                .score(20)
                .build();

        wellbeingTip1 = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        wellbeingTip2 = WellbeingTip.builder()
                .id(2L)
                .category(QuestionCategory.MENTAL)
                .score(20)
                .tipText("Practice mindfulness")
                .build();

        wellbeingTipDTO1 = WellbeingTipDTO.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        wellbeingTipDTO2 = WellbeingTipDTO.builder()
                .category(QuestionCategory.MENTAL)
                .score(20)
                .tipText("Practice mindfulness")
                .build();
    }

    @Test
    void findByCategoryAndScore_shouldReturnTips_whenTipsFound() {
        List<DailyQuestion> questions = List.of(dailyQuestion1, dailyQuestion2);

        when(wellbeingTipRepository.findByCategoryAndScore(QuestionCategory.PHYSICAL, 10))
                .thenReturn(Optional.of(wellbeingTip1));
        when(wellbeingTipRepository.findByCategoryAndScore(QuestionCategory.MENTAL, 20))
                .thenReturn(Optional.of(wellbeingTip2));
        when(mapper.map(wellbeingTip1, WellbeingTipDTO.class)).thenReturn(wellbeingTipDTO1);
        when(mapper.map(wellbeingTip2, WellbeingTipDTO.class)).thenReturn(wellbeingTipDTO2);

        List<WellbeingTipDTO> result = wellbeingTipService.findByCategoryAndScore(questions);

        verify(wellbeingTipRepository).findByCategoryAndScore(QuestionCategory.PHYSICAL, 10);
        verify(wellbeingTipRepository).findByCategoryAndScore(QuestionCategory.MENTAL, 20);
        verify(mapper).map(wellbeingTip1, WellbeingTipDTO.class);
        verify(mapper).map(wellbeingTip2, WellbeingTipDTO.class);

        assertThat(result)
                .hasSize(2)
                .containsExactly(wellbeingTipDTO1, wellbeingTipDTO2);
    }

    @Test
    void findByCategoryAndScore_shouldReturnEmptyList_whenNoTipsFound() {
        List<DailyQuestion> questions = List.of(dailyQuestion1);

        when(wellbeingTipRepository.findByCategoryAndScore(QuestionCategory.PHYSICAL, 10))
                .thenReturn(Optional.empty());

        List<WellbeingTipDTO> result = wellbeingTipService.findByCategoryAndScore(questions);

        verify(wellbeingTipRepository).findByCategoryAndScore(QuestionCategory.PHYSICAL, 10);
        verifyNoInteractions(mapper);

        assertThat(result).isEmpty();
    }

    @Test
    void findByCategoryAndScore_shouldReturnPartialList_whenSomeTipsNotFound() {
        List<DailyQuestion> questions = List.of(dailyQuestion1, dailyQuestion2);

        when(wellbeingTipRepository.findByCategoryAndScore(QuestionCategory.PHYSICAL, 10))
                .thenReturn(Optional.of(wellbeingTip1));
        when(wellbeingTipRepository.findByCategoryAndScore(QuestionCategory.MENTAL, 20))
                .thenReturn(Optional.empty());
        when(mapper.map(wellbeingTip1, WellbeingTipDTO.class)).thenReturn(wellbeingTipDTO1);

        List<WellbeingTipDTO> result = wellbeingTipService.findByCategoryAndScore(questions);

        verify(wellbeingTipRepository).findByCategoryAndScore(QuestionCategory.PHYSICAL, 10);
        verify(wellbeingTipRepository).findByCategoryAndScore(QuestionCategory.MENTAL, 20);
        verify(mapper).map(wellbeingTip1, WellbeingTipDTO.class);
        verifyNoMoreInteractions(mapper);

        assertThat(result)
                .hasSize(1)
                .containsExactly(wellbeingTipDTO1);
    }

    @Test
    void findByCategoryAndScore_shouldReturnEmptyList_whenInputListIsEmpty() {
        List<DailyQuestion> questions = Collections.emptyList();

        List<WellbeingTipDTO> result = wellbeingTipService.findByCategoryAndScore(questions);

        verifyNoInteractions(wellbeingTipRepository, mapper);

        assertThat(result).isEmpty();
    }

    @Test
    void findByCategoryAndScore_shouldReturnEmptyList_whenInputListIsNull() {
        List<WellbeingTipDTO> result = wellbeingTipService.findByCategoryAndScore(null);

        verifyNoInteractions(wellbeingTipRepository, mapper);

        assertThat(result).isEmpty();
    }

    @Test
    void calculateStreak_shouldReturnCorrectStreak_whenDailyChecksAreConsecutive() {
        dailyChecks = List.of(
                DailyCheck.builder().checkInDate(LocalDate.now()).completed(true).build(),
                DailyCheck.builder().checkInDate(LocalDate.now().minusDays(1L)).completed(true).build(),
                DailyCheck.builder().checkInDate(LocalDate.now().minusDays(2L)).completed(true).build()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dailyCheckRepository.findAllByUser(user)).thenReturn(dailyChecks);

        Integer result = wellbeingTipService.calculateStreak(1L);

        assertThat(result).isEqualTo(3);
    }

    @Test
    void calculateStreak_shouldStopStreak_whenUncompletedDayExists() {
        dailyChecks = List.of(
                DailyCheck.builder().checkInDate(LocalDate.now()).completed(true).build(),
                DailyCheck.builder().checkInDate(LocalDate.now().minusDays(1L)).completed(false).build(), // streak staje
                DailyCheck.builder().checkInDate(LocalDate.now().minusDays(2L)).completed(true).build()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dailyCheckRepository.findAllByUser(user)).thenReturn(dailyChecks);

        Integer result = wellbeingTipService.calculateStreak(1L);

        assertThat(result).isEqualTo(1);
    }

    @Test
    void calculateStreak_shouldIgnoreTodayIfNotCheckedIn() {
        dailyChecks = List.of(
                DailyCheck.builder().checkInDate(LocalDate.now()).completed(false).build(),
                DailyCheck.builder().checkInDate(LocalDate.now().minusDays(1L)).completed(true).build()
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dailyCheckRepository.findAllByUser(user)).thenReturn(dailyChecks);

        Integer result = wellbeingTipService.calculateStreak(1L);

        assertThat(result).isEqualTo(1);
    }

    @Test
    void calculateStreak_shouldReturnZero_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        int result = wellbeingTipService.calculateStreak(1L);

        assertThat(result).isZero();
    }
}
