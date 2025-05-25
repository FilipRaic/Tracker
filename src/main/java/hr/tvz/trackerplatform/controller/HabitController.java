package hr.tvz.trackerplatform.controller;

import hr.tvz.trackerplatform.model.HabitDTO;
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

    @PostMapping
    public ResponseEntity<HabitDTO> createHabit(@RequestBody HabitDTO habitDTO) {
        HabitDTO createdHabit = habitService.create(habitDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHabit);
    }
}
