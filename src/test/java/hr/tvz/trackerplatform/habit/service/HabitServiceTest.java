package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private UserSecurity userSecurity;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private HabitCompletionService habitCompletionService;
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
        assertEquals(habit.getBegin(), result.getFirst().getStartDate());
        assertEquals(habit.getDescription(), result.getFirst().getNotes());
    }

    @Test
    void testCreateHabit() {
        User user = new User();
        LocalDate startDate = LocalDate.now();
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO(null, "Read", startDate, weeklyFrequency.getName(), "Book");
        Habit habit = new Habit(1L, "Read", startDate, "Book", weeklyFrequency, user);
        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(weeklyFrequency.getName())).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(habit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getFrequency(), result.getFrequency());
        assertEquals(dto.getStartDate(), result.getStartDate());
        assertEquals(dto.getNotes(), result.getNotes());
        verify(habitCompletionService).fillMissingHabitCompletions(habit);
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
    void deleteHabit_shouldThrow_whenHabitNotFound() {
        when(habitRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitService.deleteHabit(100L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Habit should exist");
    }

    @Test
    void create_shouldHandleDailyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusDays(2);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        HabitDTO dto = new HabitDTO(null, "Walk", startDate, dailyFrequency.getName(), "Exercise");
        Habit savedHabit = new Habit(1L, "Walk", startDate, "Exercise", dailyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(dailyFrequency.getName())).thenReturn(Optional.of(dailyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertEquals("Walk", result.getName());
        assertEquals(dailyFrequency.getName(), result.getFrequency());
        verify(habitCompletionService).fillMissingHabitCompletions(savedHabit);
    }

    @Test
    void create_shouldHandleWeeklyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusWeeks(2);
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO(null, "Clean", startDate, weeklyFrequency.getName(), "Housework");
        Habit savedHabit = new Habit(1L, "Clean", startDate, "Housework", weeklyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(weeklyFrequency.getName())).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertEquals("Clean", result.getName());
        assertEquals(weeklyFrequency.getName(), result.getFrequency());
        verify(habitCompletionService).fillMissingHabitCompletions(savedHabit);
    }

    @Test
    void create_shouldHandleMonthlyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusMonths(2);
        HabitFrequency monthlyFrequency = new HabitFrequency(1, "month");
        HabitDTO dto = new HabitDTO(null, "Budget", startDate, monthlyFrequency.getName(), "Finance");
        Habit savedHabit = new Habit(1L, "Budget", startDate, "Finance", monthlyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(monthlyFrequency.getName())).thenReturn(Optional.of(monthlyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertEquals("Budget", result.getName());
        assertEquals(monthlyFrequency.getName(), result.getFrequency());
        verify(habitCompletionService).fillMissingHabitCompletions(savedHabit);
    }

    @Test
    void create_shouldHandleYearlyFrequency() {
        User user = new User();
        LocalDate startDate = LocalDate.now().minusYears(2);
        HabitFrequency yearlyFrequency = new HabitFrequency(1, "year");
        HabitDTO dto = new HabitDTO(null, "Review", startDate, yearlyFrequency.getName(), "Goals");
        Habit savedHabit = new Habit(1L, "Review", startDate, "Goals", yearlyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(yearlyFrequency.getName())).thenReturn(Optional.of(yearlyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertEquals("Review", result.getName());
        assertEquals(yearlyFrequency.getName(), result.getFrequency());
        verify(habitCompletionService).fillMissingHabitCompletions(savedHabit);
    }

    @Test
    void create_shouldHandleFutureStartDate() {
        User user = new User();
        LocalDate startDate = LocalDate.now().plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        HabitDTO dto = new HabitDTO(null, "Walk", startDate, dailyFrequency.getName(), "Exercise");
        Habit savedHabit = new Habit(1L, "Walk", startDate, "Exercise", dailyFrequency, user);

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(habitFrequencyRepository.findByName(dailyFrequency.getName())).thenReturn(Optional.of(dailyFrequency));
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);
        doNothing().when(habitCompletionService).fillMissingHabitCompletions(any(Habit.class));

        HabitDTO result = habitService.create(dto);

        assertEquals("Walk", result.getName());
        assertEquals(dailyFrequency.getName(), result.getFrequency());
        assertEquals(startDate, result.getStartDate());
        verify(habitCompletionService).fillMissingHabitCompletions(savedHabit);
    }
}
