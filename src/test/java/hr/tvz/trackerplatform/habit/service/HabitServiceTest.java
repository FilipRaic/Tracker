package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
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
class HabitServiceTest {

    @Mock
    private UserSecurity userSecurity;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private HabitFrequencyRepository habitFrequencyRepository;
    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @InjectMocks
    private HabitServiceImpl habitService;

    @Test
    void testFindAllReturnsDTOs() {
        User user = new User();
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Run", LocalDate.now(), "Daily jogging", dailyFrequency, user);
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitRepository.findAllByUser(user)).thenReturn(List.of(habit));

        List<HabitDTO> result = habitService.findAll();

        assertEquals(1, result.size());
        assertEquals("Run", result.getFirst().getName());
        assertEquals(habit.getHabitFrequency().getName(), result.getFirst().getFrequency());
    }

    @Test
    void testCreateHabit() {
        User user = new User();
        LocalDate startDate = LocalDate.now();
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO(1L, "Read", startDate, weeklyFrequency.getName(), "Book");
        Habit habit = new Habit(1L, "Read", startDate, "Book", weeklyFrequency, user);
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(weeklyFrequency.getName())).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any())).thenReturn(habit);

        HabitDTO result = habitService.create(dto);

        assertEquals(dto, result);
    }

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
        HabitCompletion secondHabitCompletion = new HabitCompletion(1L, completionDate, true, secondHabit);
        HabitCompletion thirdHabitCompletion = new HabitCompletion(1L, completionDate, false, thirdHabit);
        HabitStatusDTO firstHabitStatusDTO = buildHabitStatusDTO(firstHabit, completionDate, false);
        HabitStatusDTO secondHabitStatusDTO = buildHabitStatusDTO(secondHabit, completionDate, true);
        HabitStatusDTO thirdHabitStatusDTO = buildHabitStatusDTO(thirdHabit, completionDate, false);
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitRepository.findAllByUser(user)).thenReturn(List.of(firstHabit, secondHabit, thirdHabit));
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(true);
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(firstHabit), any())).thenReturn(Optional.of(firstHabitCompletion));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(secondHabit), any())).thenReturn(Optional.of(secondHabitCompletion));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(thirdHabit), any())).thenReturn(Optional.of(thirdHabitCompletion));

        List<HabitStatusDTO> currentHabitsWithStatus = habitService.findCurrentHabitsWithStatus();

        assertEquals(3, currentHabitsWithStatus.size());
        assertEquals(firstHabitStatusDTO, currentHabitsWithStatus.get(0));
        assertEquals(secondHabitStatusDTO, currentHabitsWithStatus.get(1));
        assertEquals(thirdHabitStatusDTO, currentHabitsWithStatus.get(2));
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
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(habit), any())).thenReturn(Optional.of(habitCompletion));
        when(habitCompletionRepository.save(changedHabitCompletion)).thenReturn(changedHabitCompletion);
        HabitStatusDTO expectedStatus = buildHabitStatusDTO(habit, completionDate, true);

        HabitStatusDTO habitStatusDTO = habitService.changeHabitStatus(habitId);

        assertEquals(expectedStatus, habitStatusDTO);
    }

    @Test
    void deleteHabit() {
        User user = new User();
        Long habitId = 1L;
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(habitId, "Workout", LocalDate.now(), null, dailyFrequency, user);
        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));

        habitService.deleteHabit(habitId);

        verify(habitRepository).findById(habitId);
        verify(habitCompletionRepository).deleteByHabit(habit);
        verify(habitRepository).delete(habit);
    }

    @Test
    void create_shouldThrowException_whenHabitFrequencyNotFound() {
        HabitDTO dto = new HabitDTO(null, "Meditate", LocalDate.now(), "non-existent-frequency", "Calm mind");
        when(habitFrequencyRepository.findByName("non-existent-frequency")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.create(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit frequency not found!");
    }

    @Test
    void findCurrentHabitsWithStatus_shouldThrow_whenHabitCompletionMissing() {
        User user = new User();
        HabitFrequency frequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Read", LocalDate.now(), "Books", frequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitRepository.findAllByUser(user)).thenReturn(List.of(habit));
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(true);
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.findCurrentHabitsWithStatus())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit completion should exist");
    }

    @Test
    void changeHabitStatus_shouldThrow_whenHabitNotFound() {
        when(habitRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.changeHabitStatus(42L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit should exist");
    }

    @Test
    void deleteHabit_shouldThrow_whenHabitNotFound() {
        when(habitRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.deleteHabit(100L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit should exist");
    }

    @Test
    void create_shouldHandleMonthlyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusMonths(2);
        HabitFrequency monthly = new HabitFrequency(1, "month");
        HabitDTO dto = new HabitDTO(null, "Budget", startDate, monthly.getName(), "Finance");
        Habit savedHabit = new Habit(1L, "Budget", startDate, "Finance", monthly, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(monthly.getName())).thenReturn(Optional.of(monthly));
        when(habitRepository.save(any())).thenReturn(savedHabit);

        HabitDTO result = habitService.create(dto);

        assertEquals("Budget", result.getName());
        verify(habitCompletionRepository, times(2)).save(any());
    }

    @Test
    void changeHabitStatus_shouldThrow_whenHabitCompletionNotFound() {
        Long habitId = 1L;
        Habit habit = new Habit(habitId, "Meditate", LocalDate.now(), null, new HabitFrequency(1, "day"), new User());
        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.changeHabitStatus(habitId))
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
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(eq(habit), any()))
                .thenReturn(Optional.of(habitCompletion));
        when(habitCompletionRepository.save(changedHabitCompletion)).thenReturn(changedHabitCompletion);
        HabitStatusDTO expectedStatus = buildHabitStatusDTO(habit, completionDate, false);

        HabitStatusDTO result = habitService.changeHabitStatus(habitId);

        assertEquals(expectedStatus, result);
        verify(habitCompletionRepository).save(changedHabitCompletion);
    }

    @Test
    void create_shouldCreateCompletionsWithCorrectDates_forDailyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusDays(2);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        HabitDTO dto = new HabitDTO(null, "Walk", startDate, dailyFrequency.getName(), "Exercise");
        Habit savedHabit = new Habit(1L, "Walk", startDate, "Exercise", dailyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(dailyFrequency.getName())).thenReturn(Optional.of(dailyFrequency));
        when(habitRepository.save(any())).thenReturn(savedHabit);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitService.create(dto);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(1))));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusDays(2))));
    }

    @Test
    void create_shouldCreateCompletionsWithCorrectDates_forWeeklyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusWeeks(2);
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO(null, "Clean", startDate, weeklyFrequency.getName(), "Housework");
        Habit savedHabit = new Habit(1L, "Clean", startDate, "Housework", weeklyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(weeklyFrequency.getName())).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any())).thenReturn(savedHabit);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitService.create(dto);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusWeeks(1))));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusWeeks(2))));
    }

    @Test
    void create_shouldCreateCompletionsWithCorrectDates_forMonthlyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusMonths(2);
        HabitFrequency monthlyFrequency = new HabitFrequency(1, "month");
        HabitDTO dto = new HabitDTO(null, "Budget", startDate, monthlyFrequency.getName(), "Finance");
        Habit savedHabit = new Habit(1L, "Budget", startDate, "Finance", monthlyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(monthlyFrequency.getName())).thenReturn(Optional.of(monthlyFrequency));
        when(habitRepository.save(any())).thenReturn(savedHabit);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitService.create(dto);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusMonths(1))));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusMonths(2))));
    }

    @Test
    void create_shouldCreateCompletionsWithCorrectDates_forYearlyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusYears(2);
        HabitFrequency yearlyFrequency = new HabitFrequency(1, "year");
        HabitDTO dto = new HabitDTO(null, "Review", startDate, yearlyFrequency.getName(), "Goals");
        Habit savedHabit = new Habit(1L, "Review", startDate, "Goals", yearlyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(yearlyFrequency.getName())).thenReturn(Optional.of(yearlyFrequency));
        when(habitRepository.save(any())).thenReturn(savedHabit);
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(false);
        when(habitCompletionRepository.save(any(HabitCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        habitService.create(dto);

        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusYears(1))));
        verify(habitCompletionRepository).save(argThat(completion ->
                completion.getCompletionDate().equals(startDate.plusYears(2))));
    }

    @Test
    void create_shouldThrow_forInvalidFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now();
        HabitFrequency invalidFrequency = new HabitFrequency(1, "invalid");
        HabitDTO dto = new HabitDTO(null, "Test", startDate, invalidFrequency.getName(), "Note");
        Habit savedHabit = new Habit(1L, "Test", startDate, "Note", invalidFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(invalidFrequency.getName())).thenReturn(Optional.of(invalidFrequency));
        when(habitRepository.save(any())).thenReturn(savedHabit);

        assertThatThrownBy(() -> habitService.create(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Habit Frequency unknown");
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
