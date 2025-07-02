package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;

import java.util.List;

public interface HabitStatusService {

    List<HabitStatusDTO> findCurrentHabitsWithStatus();

    HabitStatusDTO changeHabitStatus(Long habitId);
}
