package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryService {
    List<JournalEntryDTO> findAll();

    JournalEntryDTO create(JournalEntryDTO journalEntryDTO);

    JournalEntryDTO findByDate(LocalDate date);

    void deleteByDate(LocalDate date);

    JournalEntryDTO update(JournalEntryDTO journalEntryDTO, LocalDate date);
}
