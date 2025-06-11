package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    public List<JournalEntryDTO> findAll() {
        return journalEntryRepository.findAll().stream()
                .map(this::mapToJournalEntryDTO)
                .toList();
    }

    @Override
    public JournalEntryDTO create(JournalEntryDTO journalEntryDTO) {
        JournalEntry journalEntry = journalEntryRepository.save(mapToJournalEntry(journalEntryDTO));
        return mapToJournalEntryDTO(journalEntry);
    }

    @Override
    public JournalEntryDTO findByDate(LocalDate date) {
        return mapToJournalEntryDTO(journalEntryRepository.findByDate(date));
    }

    @Override
    public void delete(LocalDate date) {
        Long id = journalEntryRepository.findByDate(date).getId();
        journalEntryRepository.deleteById(id);
    }

    @Override
    public JournalEntryDTO update(JournalEntryDTO journalEntryDTO, LocalDate date) {
        Optional<JournalEntry> journalEntryOptional = Optional.ofNullable(journalEntryRepository.findByDate(date));

        if (journalEntryOptional.isPresent()) {
            JournalEntry journalEntryToUpdate = journalEntryOptional.get();
            journalEntryToUpdate.setDescription(journalEntryDTO.getDescription());
            journalEntryRepository.save(journalEntryToUpdate);
            return mapToJournalEntryDTO(journalEntryToUpdate);
        } else {
            throw new EntityNotFoundException("Journal entry not found.");
        }
    }

    private JournalEntryDTO mapToJournalEntryDTO(JournalEntry journalEntry) {
        if (journalEntry == null) {
            return null;
        }

        return JournalEntryDTO.builder()
                .date(journalEntry.getDate())
                .description(journalEntry.getDescription())
                .build();
    }

    private JournalEntry mapToJournalEntry(JournalEntryDTO journalEntryDTO) {
        return JournalEntry.builder()
                .date(journalEntryDTO.getDate())
                .description(journalEntryDTO.getDescription())
                .build();
    }
}
