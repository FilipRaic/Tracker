package hr.tvz.trackerplatform.service;

import hr.tvz.trackerplatform.model.JournalEntry;
import hr.tvz.trackerplatform.model.JournalEntryDTO;
import hr.tvz.trackerplatform.repository.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JournalEntryServiceImpl implements JournalEntryService{

    private final JournalEntryRepository journalEntryRepository;
    @Override
    public List<JournalEntryDTO> findAll() {
        return journalEntryRepository.findAll().stream()
                .map(this::mapToJournalEntryDTO)
                .collect(Collectors.toList());
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
    public void deleteByDate(LocalDate date) {
        journalEntryRepository.deleteByDate(date);
    }

    @Override
    public JournalEntryDTO update(JournalEntryDTO journalEntryDTO, LocalDate date) {
            Optional<JournalEntry> journalEntryOptional = Optional.ofNullable(journalEntryRepository.findByDate(date));

            if(journalEntryOptional.isPresent()) {
                JournalEntry journalEntryToUpdate = journalEntryOptional.get();
                journalEntryToUpdate.setDescription(journalEntryDTO.getDescription());
                journalEntryRepository.save(journalEntryToUpdate);
                return mapToJournalEntryDTO(journalEntryToUpdate);
            }
            else {
                throw new EntityNotFoundException("Journal entry not found.");
            }
    }

    private JournalEntryDTO mapToJournalEntryDTO(JournalEntry journalEntry){
        return JournalEntryDTO.builder()
                .date(journalEntry.getDate())
                .description(journalEntry.getDescription())
                .build();
    }
    private JournalEntry mapToJournalEntry(JournalEntryDTO journalEntryDTO){
        return JournalEntry.builder()
                .date(journalEntryDTO.getDate())
                .description(journalEntryDTO.getDescription())
                .build();
    }
}
