package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;

import java.time.LocalDate;

public interface HabitCompletionService {

    void fillMissingHabitCompletions(Habit habit);

    HabitCompletion findCurrentHabitCompletionOrThrowException(Habit habit);

    default LocalDate calculateNextCompletionDate(LocalDate startDate, HabitFrequency habitFrequency) {
        return switch (habitFrequency.getName()) {
            case "day" -> startDate.plusDays(1);
            case "week" -> startDate.plusWeeks(1);
            case "month" -> startDate.plusMonths(1);
            case "year" -> startDate.plusYears(1);
            default -> throw new IllegalStateException("Habit Frequency unknown");
        };
    }
}
