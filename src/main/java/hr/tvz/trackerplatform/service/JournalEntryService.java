package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.JournalEntryDTO;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryService {
    List<JournalEntryDTO> findAll();
    JournalEntryDTO create(JournalEntryDTO journalEntryDTO);
    JournalEntryDTO findByDate(LocalDate date);
    void delete(LocalDate date);

    JournalEntryDTO update(JournalEntryDTO journalEntryDTO,LocalDate date);
}
