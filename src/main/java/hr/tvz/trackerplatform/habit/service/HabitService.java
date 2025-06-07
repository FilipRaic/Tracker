package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;

import java.util.List;

public interface HabitService {
    List<HabitDTO> findAll();

    List<HabitStatusDTO> findCurrentHabitsWithStatus();

    HabitDTO create(HabitDTO habitDTO);

    HabitStatusDTO changeHabitStatus(Long habitId);

    void deleteHabit(Long habitId);
}
