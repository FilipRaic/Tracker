package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitStatusServiceImpl implements HabitStatusService {

    private final UserSecurity userSecurity;
    private final HabitRepository habitRepository;
    private final HabitCompletionService habitCompletionService;
    private final HabitCompletionRepository habitCompletionRepository;

    @Override
    public List<HabitStatusDTO> findCurrentHabitsWithStatus() {
        List<HabitStatusDTO> habitStatusDTOS = new ArrayList<>();
        User currentUser = userSecurity.getCurrentUser();
        List<Habit> habits = habitRepository.findAllByUser(currentUser);

        for (Habit habit : habits) {
            habitCompletionService.fillMissingHabitCompletions(habit);
            HabitCompletion currentHabitCompletion = habitCompletionService.findCurrentHabitCompletionOrThrowException(habit);
            habitStatusDTOS.add(mapToHabitWithStatusDTO(currentHabitCompletion));
        }

        return habitStatusDTOS;
    }

    @Override
    public HabitStatusDTO changeHabitStatus(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new EntityNotFoundException("Habit should exist"));
        HabitCompletion habitCompletion = habitCompletionService.findCurrentHabitCompletionOrThrowException(habit);
        habitCompletion.setDone(!habitCompletion.getDone());
        HabitCompletion updatedHabitCompletion = habitCompletionRepository.save(habitCompletion);

        return mapToHabitWithStatusDTO(updatedHabitCompletion);
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
}
