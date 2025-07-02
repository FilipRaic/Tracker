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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final UserSecurity userSecurity;
    private final HabitRepository habitRepository;
    private final HabitCompletionService habitCompletionService;
    private final HabitFrequencyRepository habitFrequencyRepository;
    private final HabitCompletionRepository habitCompletionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HabitDTO> findAll() {
        User currentUser = userSecurity.getCurrentUser();

        return habitRepository.findAllByUser(currentUser).stream()
                .map(this::mapToHabitDTO)
                .toList();
    }

    @Override
    @Transactional
    public HabitDTO create(HabitDTO habitDTO) {
        Habit habit = habitRepository.save(mapToHabit(habitDTO));
        habitCompletionService.fillMissingHabitCompletions(habit);

        return mapToHabitDTO(habit);
    }

    @Override
    @Transactional
    public void deleteHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit should exist"));

        habitCompletionRepository.deleteByHabit(habit);
        habitRepository.delete(habit);
    }

    private Habit mapToHabit(HabitDTO habitDTO) {
        HabitFrequency habitFrequency = habitFrequencyRepository.findByName(habitDTO.getFrequency())
                .orElseThrow(() -> new EntityNotFoundException("Habit frequency not found!"));
        User currentUser = userSecurity.getCurrentUser();

        return Habit.builder()
                .name(habitDTO.getName())
                .habitFrequency(habitFrequency)
                .begin(habitDTO.getStartDate())
                .description(habitDTO.getNotes())
                .user(currentUser)
                .build();
    }

    private HabitDTO mapToHabitDTO(Habit habit) {
        return HabitDTO.builder()
                .id(habit.getId())
                .name(habit.getName())
                .frequency(habit.getHabitFrequency().getName())
                .startDate(habit.getBegin())
                .notes(habit.getDescription())
                .build();
    }
}
