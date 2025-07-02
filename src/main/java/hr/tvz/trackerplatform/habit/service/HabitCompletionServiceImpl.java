package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HabitCompletionServiceImpl implements HabitCompletionService {

    private final HabitCompletionRepository habitCompletionRepository;

    @Override
    @Transactional
    public void fillMissingHabitCompletions(Habit habit) {
        HabitFrequency habitFrequency = habit.getHabitFrequency();
        LocalDate today = LocalDate.now();
        LocalDate completionDate = habit.getBegin();

        do {
            completionDate = calculateNextCompletionDate(completionDate, habitFrequency);
            addHabitCompletionIfNotPresent(habit, completionDate);
        } while (completionDate.isBefore(today));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitCompletion findCurrentHabitCompletionOrThrowException(Habit habit) {
        LocalDate today = LocalDate.now();

        return habitCompletionRepository
                .findFirstByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(habit, today)
                .orElseThrow(() -> new EntityNotFoundException("Habit completion should exist"));
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
}
