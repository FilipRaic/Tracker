package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.HabitDTO;
import hr.tvz.trackerplatform.model.HabitStatusDTO;

import java.util.List;

public interface HabitService {
    List<HabitDTO> findAll();

    List<HabitStatusDTO> findCurrentHabitsWithStatus();

    HabitDTO create(HabitDTO habitDTO);

    HabitStatusDTO changeHabitStatus(Long habitId);

    void deleteHabit(Long habitId);
}
