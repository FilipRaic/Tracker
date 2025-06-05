package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitFrequencyRepository habitFrequencyRepository;
    private final HabitCompletionRepository habitCompletionRepository;

    @Override
    public List<HabitDTO> findAll() {
        return habitRepository.findAll().stream()
                .map(this::mapToHabitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitStatusDTO> findCurrentHabitsWithStatus() {
        List<HabitStatusDTO> habitStatusDTOS = new ArrayList<>();
        List<Habit> habits = habitRepository.findAll();

        for (Habit habit : habits) {
            fillMissingHabitCompletions(habit);
            HabitCompletion currentHabitCompletion = findCurrentHabitCompletionOrThrowException(habit);
            habitStatusDTOS.add(mapToHabitWithStatusDTO(currentHabitCompletion));
        }
        return habitStatusDTOS;
    }

    @Override
    public HabitDTO create(HabitDTO habitDTO) {
        Habit habit = habitRepository.save(mapToHabit(habitDTO));
        fillMissingHabitCompletions(habit);
        return mapToHabitDTO(habit);
    }

    @Override
    public HabitStatusDTO changeHabitStatus(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit should exist"));
        HabitCompletion habitCompletion = findCurrentHabitCompletionOrThrowException(habit);
        habitCompletion.setDone(!habitCompletion.getDone());
        HabitCompletion updatedHabitCompletion = habitCompletionRepository.save(habitCompletion);
        return mapToHabitWithStatusDTO(updatedHabitCompletion);
    }

    @Override
    @Transactional
    public void deleteHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit should exist"));
        habitCompletionRepository.deleteByHabit(habit);
        habitRepository.delete(habit);
    }

    private HabitCompletion findCurrentHabitCompletionOrThrowException(Habit habit) {
        LocalDate today = LocalDate.now();
        return habitCompletionRepository
                .findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(habit, today)
                .orElseThrow(() -> new EntityNotFoundException("Habit completion should exist"));
    }

    private void fillMissingHabitCompletions(Habit habit) {
        HabitFrequency habitFrequency = habit.getHabitFrequency();
        LocalDate today = LocalDate.now();
        LocalDate completionDate = habit.getBegin();

        do {
            completionDate = calculateNextCompletionDate(completionDate, habitFrequency);
            addHabitCompletionIfNotPresent(habit, completionDate);
        } while (completionDate.isBefore(today));
    }

    private void addHabitCompletionIfNotPresent(Habit habit, LocalDate completionDate) {
        if (!habitCompletionRepository.existsByHabitAndCompletionDate(habit, completionDate)) {
            addNewHabitCompletion(habit, completionDate);
        }
    }

    private void addNewHabitCompletion(Habit habit, LocalDate completionDate) {
        HabitCompletion habitCompletion = HabitCompletion.builder()
                .habit(habit)
                .completionDate(completionDate)
                .done(false)
                .build();
        habitCompletionRepository.save(habitCompletion);
    }

    private LocalDate calculateNextCompletionDate(LocalDate startDate, HabitFrequency habitFrequency) {
        return switch (habitFrequency.getName()) {
            case "day" -> startDate.plusDays(1);
            case "week" -> startDate.plusWeeks(1);
            case "month" -> startDate.plusMonths(1);
            case "year" -> startDate.plusYears(1);
            default -> throw new IllegalStateException("Habit Frequency unknown");
        };
    }

    private HabitStatusDTO mapToHabitWithStatusDTO(HabitCompletion habitCompletion) {
        return mapToHabitWithStatusDTO(habitCompletion.getHabit(), habitCompletion.getCompletionDate(),
                habitCompletion.getDone());
    }

    private HabitStatusDTO mapToHabitWithStatusDTO(Habit habit, LocalDate completionDate, boolean done) {
        return HabitStatusDTO.builder()
                .id(habit.getId())
                .name(habit.getName())
                .frequency(habit.getHabitFrequency().getName())
                .startDate(habit.getBegin())
                .notes(habit.getDescription())
                .dueDate(completionDate)
                .done(done)
                .build();
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
                .id(habit.getId())
                .name(habit.getName())
                .frequency(habit.getHabitFrequency().getName())
                .startDate(habit.getBegin())
                .notes(habit.getDescription())
                .build();
    }
}
