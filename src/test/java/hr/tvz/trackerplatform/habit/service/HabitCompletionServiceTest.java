package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitCompletionServiceTest {

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @InjectMocks
    private HabitCompletionServiceImpl habitCompletionService;

    @Test
    void fillMissingHabitCompletions_shouldCreateDailyCompletions() {
        LocalDate startDate = LocalDate.now().minusDays(3);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Walk", startDate, "Exercise", dailyFrequency, null);

        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitCompletionService.fillMissingHabitCompletions(habit);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(1)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(2)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(3)) && !completion.getDone()));
    }

    @Test
    void fillMissingHabitCompletions_shouldCreateWeeklyCompletions() {
        LocalDate startDate = LocalDate.now().minusWeeks(3);
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        Habit habit = new Habit(1L, "Workout", startDate, "Fitness", weeklyFrequency, null);

        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitCompletionService.fillMissingHabitCompletions(habit);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusWeeks(1)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusWeeks(2)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusWeeks(3)) && !completion.getDone()));
    }

    @Test
    void fillMissingHabitCompletions_shouldCreateMonthlyCompletions() {
        LocalDate startDate = LocalDate.now().minusMonths(3);
        HabitFrequency monthlyFrequency = new HabitFrequency(1, "month");
        Habit habit = new Habit(1L, "Budget", startDate, "Finance", monthlyFrequency, null);

        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitCompletionService.fillMissingHabitCompletions(habit);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusMonths(1)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusMonths(2)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusMonths(3)) && !completion.getDone()));
    }

    @Test
    void fillMissingHabitCompletions_shouldCreateYearlyCompletions() {
        LocalDate startDate = LocalDate.now().minusYears(2);
        HabitFrequency yearlyFrequency = new HabitFrequency(1, "year");
        Habit habit = new Habit(1L, "Review", startDate, "Goals", yearlyFrequency, null);

        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitCompletionService.fillMissingHabitCompletions(habit);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusYears(1)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusYears(2)) && !completion.getDone()));
    }

    @Test
    void fillMissingHabitCompletions_shouldNotCreateDuplicateCompletions() {
        LocalDate startDate = LocalDate.now().minusDays(3);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Walk", startDate, "Exercise", dailyFrequency, null);

        when(habitCompletionRepository.existsByHabitAndCompletionDate(eq(habit), eq(startDate.plusDays(1)))).thenReturn(true);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(eq(habit), eq(startDate.plusDays(2)))).thenReturn(false);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(eq(habit), eq(startDate.plusDays(3)))).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitCompletionService.fillMissingHabitCompletions(habit);

        verify(habitCompletionRepository, times(2)).save(any(HabitCompletion.class));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(2)) && !completion.getDone()));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(3)) && !completion.getDone()));
    }

    @Test
    void fillMissingHabitCompletions_shouldThrow_forInvalidFrequency() {
        LocalDate startDate = LocalDate.now();
        HabitFrequency invalidFrequency = new HabitFrequency(1, "invalid");
        Habit habit = new Habit(1L, "Test", startDate, "Note", invalidFrequency, null);

        assertThatThrownBy(() -> habitCompletionService.fillMissingHabitCompletions(habit))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Habit Frequency unknown");
    }

    @Test
    void findCurrentHabitCompletionOrThrowException_shouldReturnCompletion() {
        LocalDate today = LocalDate.now();
        LocalDate completionDate = today.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Walk", today, "Exercise", dailyFrequency, null);
        HabitCompletion completion = new HabitCompletion(1L, completionDate, false, 0, habit);

        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), eq(today)))
                .thenReturn(Optional.of(completion));

        HabitCompletion result = habitCompletionService.findCurrentHabitCompletionOrThrowException(habit);

        assertEquals(completion, result);
    }

    @Test
    void findCurrentHabitCompletionOrThrowException_shouldThrow_whenNotFound() {
        LocalDate today = LocalDate.now();
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Walk", today, "Exercise", dailyFrequency, null);

        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), eq(today)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitCompletionService.findCurrentHabitCompletionOrThrowException(habit))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit completion should exist");
    }
}
