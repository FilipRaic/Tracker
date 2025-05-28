package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.HabitDTO;

import java.util.List;

public interface HabitService {
    List<HabitDTO> findAll();

    HabitDTO create(HabitDTO habitDTO);
}
