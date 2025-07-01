package hr.tvz.trackerplatform.shared.scheduler;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.habit.service.HabitCompletionService;
import hr.tvz.trackerplatform.shared.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitSchedulerTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @Mock
    private HabitCompletionService habitCompletionService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private HabitScheduler habitScheduler;

    @Test
    void testFillMissingHabitCompletions() {
        Habit habit1 = new Habit();
        Habit habit2 = new Habit();

        when(habitRepository.findAll()).thenReturn(List.of(habit1, habit2));

        habitScheduler.fillMissingHabitCompletions();

        verify(habitCompletionService, times(2)).fillMissingHabitCompletions(any(Habit.class));
    }

    @Test
    void testAdjustStreaks_skipsWhenNoFailure() {
        Habit habit = new Habit();

        when(habitRepository.findAll()).thenReturn(List.of(habit));
        when(habitCompletionRepository.findFirstByHabitAndDoneFalseAndCompletionDateBeforeOrderByCompletionDateDesc(
                eq(habit), any(LocalDate.class))).thenReturn(Optional.empty());

        habitScheduler.adjustStreaks();

        verify(habitCompletionRepository, never())
                .findAllByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(any(), any());
    }

    @Test
    void testAdjustStreaks_resetsStreaksWhenFailureExists() {
        Habit habit = new Habit();
        HabitCompletion failCompletion = new HabitCompletion();
        failCompletion.setCompletionDate(LocalDate.now().minusDays(2));

        HabitCompletion hc1 = new HabitCompletion();
        hc1.setCompletionDate(LocalDate.now().minusDays(2));
        HabitCompletion hc2 = new HabitCompletion();
        hc2.setCompletionDate(LocalDate.now().minusDays(1));
        HabitCompletion hc3 = new HabitCompletion();
        hc3.setCompletionDate(LocalDate.now());

        when(habitRepository.findAll()).thenReturn(List.of(habit));
        when(habitCompletionRepository
                .findFirstByHabitAndDoneFalseAndCompletionDateBeforeOrderByCompletionDateDesc(eq(habit), any()))
                .thenReturn(Optional.of(failCompletion));
        when(habitCompletionRepository
                .findAllByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), eq(failCompletion.getCompletionDate())))
                .thenReturn(List.of(hc1, hc2, hc3));

        habitScheduler.adjustStreaks();

        verify(habitCompletionRepository, times(3)).save(any(HabitCompletion.class));
        assertThat(hc1.getStreak()).isEqualTo(0);
        assertThat(hc2.getStreak()).isEqualTo(1);
        assertThat(hc3.getStreak()).isEqualTo(2);
    }

    @Test
    void testSendStreakWarningEmail_sendsEmailWhenDueToday() {
        Habit habit = new Habit();
        HabitCompletion dueToday = new HabitCompletion();
        LocalDate today = LocalDate.now();
        dueToday.setCompletionDate(today);

        when(habitRepository.findAll()).thenReturn(List.of(habit));
        when(habitCompletionRepository
                .findFirstByHabitAndDoneFalseAndCompletionDate(eq(habit), eq(today)))
                .thenReturn(Optional.of(dueToday));

        habitScheduler.sendStreakWarningEmail();

        verify(emailService).sendStreakWarningEmail(habit);
    }

    @Test
    void testSendStreakWarningEmail_skipsIfNoDueToday() {
        Habit habit = new Habit();

        when(habitRepository.findAll()).thenReturn(List.of(habit));
        when(habitCompletionRepository
                .findFirstByHabitAndDoneFalseAndCompletionDate(eq(habit), eq(LocalDate.now())))
                .thenReturn(Optional.empty());

        habitScheduler.sendStreakWarningEmail();

        verify(emailService, never()).sendStreakWarningEmail(any());
    }
}
