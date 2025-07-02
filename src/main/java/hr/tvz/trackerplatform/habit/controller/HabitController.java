package hr.tvz.trackerplatform.habit.controller;

import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.service.HabitService;
import hr.tvz.trackerplatform.habit.service.HabitStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;
    private final HabitStatusService habitStatusService;

    @GetMapping
    public ResponseEntity<List<HabitDTO>> findAllHabits() {
        List<HabitDTO> habitDTOS = habitService.findAll();
        return ResponseEntity.ok(habitDTOS);
    }

    @GetMapping("/status")
    public ResponseEntity<List<HabitStatusDTO>> findCurrentHabitsWithStatus() {
        List<HabitStatusDTO> currentHabitsWithStatus = habitStatusService.findCurrentHabitsWithStatus();
        return ResponseEntity.ok(currentHabitsWithStatus);
    }

    @PostMapping
    public ResponseEntity<HabitDTO> createHabit(@RequestBody HabitDTO habitDTO) {
        HabitDTO createdHabit = habitService.create(habitDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHabit);
    }

    @PutMapping("/status/{habitId}")
    public ResponseEntity<HabitStatusDTO> changeHabitStatus(@PathVariable Long habitId) {
        HabitStatusDTO habitStatusDTO = habitStatusService.changeHabitStatus(habitId);
        return ResponseEntity.ok(habitStatusDTO);
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long habitId) {
        habitService.deleteHabit(habitId);
        return ResponseEntity.noContent().build();
    }
}
