package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitStatusServiceTest {

    @Mock
    private UserSecurity userSecurity;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private HabitCompletionService habitCompletionService;
    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @InjectMocks
    private HabitStatusServiceImpl habitStatusService;

    @Test
    void findCurrentHabitsWithStatus() {
        User user = new User();
        LocalDate startDate = LocalDate.now();
        LocalDate completionDate = startDate.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit firstHabit = new Habit(1L, "Workout", startDate, null, dailyFrequency, user);
        Habit secondHabit = new Habit(2L, "Lunch", startDate, null, dailyFrequency, user);
        Habit thirdHabit = new Habit(3L, "Dinner", startDate, null, dailyFrequency, user);
        HabitCompletion firstHabitCompletion = new HabitCompletion(1L, completionDate, false, firstHabit);
        HabitCompletion secondHabitCompletion = new HabitCompletion(2L, completionDate, true, secondHabit);
        HabitCompletion thirdHabitCompletion = new HabitCompletion(3L, completionDate, false, thirdHabit);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitRepository.findAllByUser(user)).thenReturn(List.of(firstHabit, secondHabit, thirdHabit));
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(firstHabit))).thenReturn(firstHabitCompletion);
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(secondHabit))).thenReturn(secondHabitCompletion);
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(thirdHabit))).thenReturn(thirdHabitCompletion);

        List<HabitStatusDTO> currentHabitsWithStatus = habitStatusService.findCurrentHabitsWithStatus();

        assertEquals(3, currentHabitsWithStatus.size());
        assertEquals(buildHabitStatusDTO(firstHabit, completionDate, false), currentHabitsWithStatus.get(0));
        assertEquals(buildHabitStatusDTO(secondHabit, completionDate, true), currentHabitsWithStatus.get(1));
        assertEquals(buildHabitStatusDTO(thirdHabit, completionDate, false), currentHabitsWithStatus.get(2));
        verify(habitCompletionService, times(3)).fillMissingHabitCompletions(any(Habit.class));
    }

    @Test
    void changeHabitStatus() {
        User user = new User();
        Long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate completionDate = today.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(habitId, "Workout", today, null, dailyFrequency, user);
        HabitCompletion habitCompletion = new HabitCompletion(1L, completionDate, false, habit);
        HabitCompletion changedHabitCompletion = new HabitCompletion(1L, completionDate, true, habit);

        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(habit))).thenReturn(habitCompletion);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenReturn(changedHabitCompletion);

        HabitStatusDTO habitStatusDTO = habitStatusService.changeHabitStatus(habitId);

        assertEquals(buildHabitStatusDTO(habit, completionDate, true), habitStatusDTO);
        verify(habitCompletionRepository).save(argThat(completion -> completion.getDone()));
    }

    @Test
    void findCurrentHabitsWithStatus_shouldThrow_whenHabitCompletionMissing() {
        User user = new User();
        HabitFrequency frequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Read", LocalDate.now(), "Books", frequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitRepository.findAllByUser(user)).thenReturn(List.of(habit));
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(habit)))
                .thenThrow(new EntityNotFoundException("Habit completion should exist"));

        assertThatThrownBy(() -> habitStatusService.findCurrentHabitsWithStatus())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit completion should exist");
    }

    @Test
    void changeHabitStatus_shouldThrow_whenHabitNotFound() {
        when(habitRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitStatusService.changeHabitStatus(42L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit should exist");
    }

    @Test
    void changeHabitStatus_shouldThrow_whenHabitCompletionNotFound() {
        Long habitId = 1L;
        Habit habit = new Habit(habitId, "Meditate", LocalDate.now(), null, new HabitFrequency(1, "day"), new User());

        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(habit)))
                .thenThrow(new EntityNotFoundException("Habit completion should exist"));

        assertThatThrownBy(() -> habitStatusService.changeHabitStatus(habitId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit completion should exist");
    }

    @Test
    void changeHabitStatus_shouldToggleTrueToFalse() {
        Long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate completionDate = today.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        User user = new User();
        Habit habit = new Habit(habitId, "Read", today, null, dailyFrequency, user);
        HabitCompletion habitCompletion = new HabitCompletion(1L, completionDate, true, habit);
        HabitCompletion changedHabitCompletion = new HabitCompletion(1L, completionDate, false, habit);

        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(habit))).thenReturn(habitCompletion);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenReturn(changedHabitCompletion);

        HabitStatusDTO result = habitStatusService.changeHabitStatus(habitId);

        assertEquals(buildHabitStatusDTO(habit, completionDate, false), result);
        verify(habitCompletionRepository).save(argThat(completion -> !completion.getDone()));
    }

    @Test
    void changeHabitStatus_shouldHandleWeeklyFrequency() {
        Long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate completionDate = today.plusWeeks(1);
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        User user = new User();
        Habit habit = new Habit(habitId, "Workout", today, null, weeklyFrequency, user);
        HabitCompletion habitCompletion = new HabitCompletion(1L, completionDate, false, habit);
        HabitCompletion changedHabitCompletion = new HabitCompletion(1L, completionDate, true, habit);

        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionService.findCurrentHabitCompletionOrThrowException(eq(habit))).thenReturn(habitCompletion);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenReturn(changedHabitCompletion);

        HabitStatusDTO result = habitStatusService.changeHabitStatus(habitId);

        assertEquals(buildHabitStatusDTO(habit, completionDate, true), result);
        verify(habitCompletionRepository).save(argThat(completion -> completion.getDone()));
    }

    private HabitStatusDTO buildHabitStatusDTO(Habit habit, LocalDate completionDate, boolean done) {
        return HabitStatusDTO.builder()
                .id(habit.getId())
                .name(habit.getName())
                .startDate(habit.getBegin())
                .frequency(habit.getHabitFrequency().getName())
                .notes(habit.getDescription())
                .dueDate(completionDate)
                .done(done)
                .build();
    }
}
