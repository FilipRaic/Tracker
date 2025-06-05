package hr.tvz.trackerplatform.controller;

import hr.tvz.trackerplatform.model.HabitDTO;
import hr.tvz.trackerplatform.model.JournalEntry;
import hr.tvz.trackerplatform.model.JournalEntryDTO;
import hr.tvz.trackerplatform.service.HabitService;
import hr.tvz.trackerplatform.service.JournalEntryService;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/journal")
public class JournalEntryController {
    private final JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntryDTO>> findAllJournalEntry() {
        List<JournalEntryDTO> journalEntryDTOS = journalEntryService.findAll();
        return ResponseEntity.ok(journalEntryDTOS);
    }
    @GetMapping("{date}")
    public ResponseEntity<JournalEntryDTO> findJournalEntryByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        JournalEntryDTO journalEntryDTO = journalEntryService.findByDate(date);
        return ResponseEntity.ok(journalEntryDTO);
    }

    @PostMapping
    public ResponseEntity<JournalEntryDTO> createJournalEntry(@RequestBody JournalEntryDTO journalEntryDTO) {
        System.out.println(journalEntryDTO.getDate());
        JournalEntryDTO createdJournalEntry = journalEntryService.create(journalEntryDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdJournalEntry);
    }
    @DeleteMapping("/{date}")
    public ResponseEntity<Void> deleteByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            journalEntryService.delete(date);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{date}")
    public ResponseEntity<JournalEntryDTO> updateJournalEntry(@PathVariable LocalDate date, @RequestBody JournalEntryDTO journalEntryDTO) {
        JournalEntryDTO updatedJournalEntryDTO = journalEntryService.update(journalEntryDTO, date);
        return new ResponseEntity<>(updatedJournalEntryDTO, HttpStatus.OK);
    }

}
