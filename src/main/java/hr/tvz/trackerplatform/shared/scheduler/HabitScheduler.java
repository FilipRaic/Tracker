package hr.tvz.trackerplatform.shared.scheduler;

import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.habit.service.HabitCompletionService;
import hr.tvz.trackerplatform.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HabitScheduler {
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final HabitCompletionService habitCompletionService;
    private final EmailService emailService;

    @Transactional
    @Scheduled(cron = "${scheduler.cron.habit-completion-fill}")
    public void fillMissingHabitCompletions() {
        log.info("Starting scheduled task for filling missing habits");
        List<Habit> habits = habitRepository.findAll();
        for (Habit habit : habits) {
            habitCompletionService.fillMissingHabitCompletions(habit);
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.cron.habit-streak-adjust}")
    public void adjustStreaks() {
        log.info("Starting scheduled task for adjusting streaks");
        LocalDate today = LocalDate.now();
        List<Habit> habits = habitRepository.findAll();
        for (Habit habit : habits) {
            Optional<HabitCompletion> lastFailedCompletion = habitCompletionRepository
                    .findFirstByHabitAndDoneFalseAndCompletionDateBeforeOrderByCompletionDateDesc(habit, today);
            if(lastFailedCompletion.isEmpty()) {
                continue;
            }
            resetStreaksForHabitFromCompletionDate(habit, lastFailedCompletion.get().getCompletionDate());
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.cron.habit-streak-warning}")
    public void sendStreakWarningEmail() {
        log.info("Starting scheduled task for sending streak warning emails");
        LocalDate today = LocalDate.now();
        List<Habit> habits = habitRepository.findAll();
        for (Habit habit : habits) {
            Optional<HabitCompletion> habitDueToday = habitCompletionRepository
                    .findFirstByHabitAndDoneFalseAndCompletionDate(habit, today);
            if(habitDueToday.isPresent()) {
                emailService.sendStreakWarningEmail(habit);
            }
        }
    }

    private void resetStreaksForHabitFromCompletionDate(Habit habit, LocalDate completionDate) {
        List<HabitCompletion> allCompletionsAfterLastFail = habitCompletionRepository
                .findAllByHabitAndCompletionDateGreaterThanEqualOrderByCompletionDateAsc(habit, completionDate);
        int streak = 0;

        for (HabitCompletion habitCompletion : allCompletionsAfterLastFail) {
            habitCompletion.setStreak(streak);
            habitCompletionRepository.save(habitCompletion);
            streak++;
        }
    }
}
