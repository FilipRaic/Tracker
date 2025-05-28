package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.Habit;
import hr.tvz.trackerplatform.model.HabitDTO;
import hr.tvz.trackerplatform.model.HabitFrequency;
import hr.tvz.trackerplatform.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.repository.HabitRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HabitServiceTest {
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private HabitFrequencyRepository habitFrequencyRepository;

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
        assertEquals("Run", result.get(0).getName());
        assertEquals(habit.getHabitFrequency().getName(), result.get(0).getFrequency());
    }

    @Test
    void testCreateHabit() {
        // given
        LocalDate startDate = LocalDate.now();
        HabitFrequency weeklyFrequency = new HabitFrequency(1, "week");
        HabitDTO dto = new HabitDTO("Read", startDate, weeklyFrequency.getName(), "Book");
        Habit habit = new Habit(1L, "Read", startDate, "Book", weeklyFrequency);
        when(habitFrequencyRepository.findByName(eq(weeklyFrequency.getName()))).thenReturn(Optional.of(weeklyFrequency));
        when(habitRepository.save(any())).thenReturn(habit);

        // when
        HabitDTO result = habitService.create(dto);

        // then
        assertEquals(dto, result);
    }
}