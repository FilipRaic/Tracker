package hr.tvz.trackerplatform.habit.service;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;

import java.util.List;

public interface HabitService {

    List<HabitDTO> findAll();

    HabitDTO create(HabitDTO habitDTO);

    void deleteHabit(Long habitId);
}
