package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalEntryServiceImpl implements JournalEntryService {

    private final Mapper mapper;
    private final UserSecurity userSecurity;
    private final JournalEntryRepository journalEntryRepository;

    @Override
    public List<JournalEntryDTO> findAll() {
        User currentUser = userSecurity.getCurrentUser();
        List<JournalEntry> journalEntries = journalEntryRepository.findAllByUser(currentUser);

        return mapper.mapList(journalEntries, JournalEntryDTO.class);
    }

    @Override
    public JournalEntryDTO create(JournalEntryDTO journalEntryDTO) {
        User currentUser = userSecurity.getCurrentUser();
        JournalEntry journalEntryToSave = mapper.map(journalEntryDTO, JournalEntry.class);
        journalEntryToSave.setUser(currentUser);
        JournalEntry journalEntry = journalEntryRepository.save(journalEntryToSave);

        return mapper.map(journalEntry, JournalEntryDTO.class);
    }

    @Override
    public JournalEntryDTO findByDate(LocalDate date) {
        User currentUser = userSecurity.getCurrentUser();
        JournalEntry journalEntry = journalEntryRepository.findByUserAndDate(currentUser, date)
                .orElseThrow(() -> new TrackerException(ErrorMessage.JOURNAL_ENTRY_NOT_FOUND));

        return mapper.map(journalEntry, JournalEntryDTO.class);
    }

    @Override
    public void delete(LocalDate date) {
        User currentUser = userSecurity.getCurrentUser();
        JournalEntry journalEntry = journalEntryRepository.findByUserAndDate(currentUser, date)
                .orElseThrow(() -> new TrackerException(ErrorMessage.JOURNAL_ENTRY_NOT_FOUND));

        journalEntryRepository.deleteById(journalEntry.getId());
    }

    @Override
    public JournalEntryDTO update(JournalEntryDTO journalEntryDTO, LocalDate date) {
        User currentUser = userSecurity.getCurrentUser();
        JournalEntry journalEntry = journalEntryRepository.findByUserAndDate(currentUser, date)
                .orElseThrow(() -> new TrackerException(ErrorMessage.JOURNAL_ENTRY_NOT_FOUND));

        journalEntry.setDescription(journalEntryDTO.getDescription());
        journalEntry = journalEntryRepository.save(journalEntry);

        return mapper.map(journalEntry, JournalEntryDTO.class);
    }
}
