package hr.tvz.trackerplatform.controller;

import hr.tvz.trackerplatform.model.HabitDTO;
import hr.tvz.trackerplatform.model.HabitStatusDTO;
import hr.tvz.trackerplatform.service.HabitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/habits")
public class HabitController {
    private final HabitService habitService;

    @GetMapping
    public ResponseEntity<List<HabitDTO>> findAllHabits() {
        List<HabitDTO> habitDTOS = habitService.findAll();
        return ResponseEntity.ok(habitDTOS);
    }

    @GetMapping("/status")
    public ResponseEntity<List<HabitStatusDTO>> findCurrentHabitsWithStatus() {
        List<HabitStatusDTO> currentHabitsWithStatus = habitService.findCurrentHabitsWithStatus();
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
        HabitStatusDTO habitStatusDTO = habitService.changeHabitStatus(habitId);
        return ResponseEntity.ok(habitStatusDTO);
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long habitId) {
        habitService.deleteHabit(habitId);
        return ResponseEntity.noContent().build();
    }
}
