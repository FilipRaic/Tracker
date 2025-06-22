package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
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
    private final UserSecurity userSecurity;

    @Override
    public List<JournalEntryDTO> findAll() {
        User currentUser = userSecurity.getCurrentUser();
        return journalEntryRepository.findAllByUser(currentUser).stream()
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
        User currentUser = userSecurity.getCurrentUser();
        return mapToJournalEntryDTO(journalEntryRepository.findByUserAndDate(currentUser, date));
    }

    @Override
    public void delete(LocalDate date) {
        User currentUser = userSecurity.getCurrentUser();
        Long id = journalEntryRepository.findByUserAndDate(currentUser, date).getId();
        journalEntryRepository.deleteById(id);
    }

    @Override
    public JournalEntryDTO update(JournalEntryDTO journalEntryDTO, LocalDate date) {
        User currentUser = userSecurity.getCurrentUser();
        Optional<JournalEntry> journalEntryOptional = Optional.ofNullable(
                journalEntryRepository.findByUserAndDate(currentUser, date));

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
        User currentUser = userSecurity.getCurrentUser();
        return JournalEntry.builder()
                .date(journalEntryDTO.getDate())
                .description(journalEntryDTO.getDescription())
                .user(currentUser)
                .build();
    }
}
