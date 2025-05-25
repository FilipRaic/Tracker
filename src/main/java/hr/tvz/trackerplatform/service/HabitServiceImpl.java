package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.Habit;
import hr.tvz.trackerplatform.model.HabitDTO;
import hr.tvz.trackerplatform.model.HabitFrequency;
import hr.tvz.trackerplatform.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class HabitServiceImpl implements HabitService{
    private final HabitRepository habitRepository;
    private final HabitFrequencyRepository habitFrequencyRepository;

    @Override
    public List<HabitDTO> findAll() {
        return habitRepository.findAll().stream()
                .map(this::mapToHabitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HabitDTO create(HabitDTO habitDTO) {
        Habit habit = habitRepository.save(mapToHabit(habitDTO));
        return mapToHabitDTO(habit);
    }

    private Habit mapToHabit(HabitDTO habitDTO) {
        HabitFrequency habitFrequency = habitFrequencyRepository.findByName(habitDTO.getFrequency())
                .orElseThrow(() -> new EntityNotFoundException("Habit frequency not found!"));
        return Habit.builder()
                .name(habitDTO.getName())
                .habitFrequency(habitFrequency)
                .begin(habitDTO.getStartDate())
                .description(habitDTO.getNotes())
                .build();
    }

    private HabitDTO mapToHabitDTO(Habit habit) {
        return HabitDTO.builder()
                .name(habit.getName())
                .frequency(habit.getHabitFrequency().getName())
                .startDate(habit.getBegin())
                .notes(habit.getDescription())
                .build();
    }
}
