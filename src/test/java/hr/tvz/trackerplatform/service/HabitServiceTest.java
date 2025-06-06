package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.habit.service.HabitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {
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
        // given
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(1L, "Run", LocalDate.now(), "Daily jogging", dailyFrequency);
        when(habitRepository.findAll()).thenReturn(List.of(habit));

        // when
        List<HabitDTO> result = habitService.findAll();

        // then
        assertEquals(1, result.size());
        assertEquals("Run", result.getFirst().getName());
        assertEquals(habit.getHabitFrequency().getName(), result.getFirst().getFrequency());
    }

    @Test
    void testCreateHabit() {
        // given
        LocalDate startDate = LocalDate.now();
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO(1L, "Read", startDate, weeklyFrequency.getName(), "Book");
        Habit habit = new Habit(1L, "Read", startDate, "Book", weeklyFrequency);
        when(habitFrequencyRepository.findByName(weeklyFrequency.getName())).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any())).thenReturn(habit);

        // when
        HabitDTO result = habitService.create(dto);

        // then
        assertEquals(dto, result);
    }

    @Test
    void findCurrentHabitsWithStatus() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate completionDate = startDate.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit firstHabit = new Habit(1L, "Workout", startDate, null, dailyFrequency);
        Habit secondHabit = new Habit(2L, "Lunch", startDate, null, dailyFrequency);
        Habit thirdHabit = new Habit(3L, "Dinner", startDate, null, dailyFrequency);
        HabitCompletion firstHabitCompletion = new HabitCompletion(1L, completionDate, false, firstHabit);
        HabitCompletion secondHabitCompletion = new HabitCompletion(1L, completionDate, true, secondHabit);
        HabitCompletion thirdHabitCompletion = new HabitCompletion(1L, completionDate, false, thirdHabit);
        HabitStatusDTO firstHabitStatusDTO = buildHabitStatusDTO(firstHabit, completionDate, false);
        HabitStatusDTO secondHabitStatusDTO = buildHabitStatusDTO(secondHabit, completionDate, true);
        HabitStatusDTO thirdHabitStatusDTO = buildHabitStatusDTO(thirdHabit, completionDate, false);
        when(habitRepository.findAll()).thenReturn(List.of(firstHabit, secondHabit, thirdHabit));
        when(habitCompletionRepository.existsByHabitAndCompletionDate(any(), any())).thenReturn(true);
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(firstHabit), any())).thenReturn(Optional.of(firstHabitCompletion));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(secondHabit), any())).thenReturn(Optional.of(secondHabitCompletion));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(thirdHabit), any())).thenReturn(Optional.of(thirdHabitCompletion));

        // when
        List<HabitStatusDTO> currentHabitsWithStatus = habitService.findCurrentHabitsWithStatus();

        // then
        assertEquals(3, currentHabitsWithStatus.size());
        assertEquals(firstHabitStatusDTO, currentHabitsWithStatus.get(0));
        assertEquals(secondHabitStatusDTO, currentHabitsWithStatus.get(1));
        assertEquals(thirdHabitStatusDTO, currentHabitsWithStatus.get(2));
    }

    @Test
    void changeHabitStatus() {
        // given
        Long habitId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate completionDate = today.plusDays(1);
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(habitId, "Workout", today, null, dailyFrequency);
        HabitCompletion habitCompletion = new HabitCompletion(1L, completionDate, false, habit);
        HabitCompletion changedHabitCompletion = new HabitCompletion(1L, completionDate, true, habit);
        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));
        when(habitCompletionRepository.findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(
                eq(habit), any())).thenReturn(Optional.of(habitCompletion));
        when(habitCompletionRepository.save(changedHabitCompletion)).thenReturn(changedHabitCompletion);
        HabitStatusDTO expectedStatus = buildHabitStatusDTO(habit, completionDate, true);

        // when
        HabitStatusDTO habitStatusDTO = habitService.changeHabitStatus(habitId);

        // then
        assertEquals(expectedStatus, habitStatusDTO);
    }

    @Test
    void deleteHabit() {
        // given
        Long habitId = 1L;
        HabitFrequency dailyFrequency = new HabitFrequency(1, "day");
        Habit habit = new Habit(habitId, "Workout", LocalDate.now(), null, dailyFrequency);
        when(habitRepository.findById(habitId)).thenReturn(Optional.of(habit));

        // when
        habitService.deleteHabit(habitId);

        // then
        verify(habitRepository).findById(habitId);
        verify(habitCompletionRepository).deleteByHabit(habit);
        verify(habitRepository).delete(habit);
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
