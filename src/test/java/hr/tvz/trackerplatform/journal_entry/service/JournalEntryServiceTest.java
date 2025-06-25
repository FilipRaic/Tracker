package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalEntryServiceTest {

    @Mock
    private Mapper mapper;
    @Mock
    private UserSecurity userSecurity;
    @Mock
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryServiceImpl journalEntryService;

    @Test
    void findAll_shouldReturnAllJournalEntries() {
        User user = new User();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        JournalEntry entry1 = JournalEntry.builder().id(1L).date(today).description("Today's entry").build();
        JournalEntry entry2 = JournalEntry.builder().id(2L).date(yesterday).description("Yesterday's entry").build();

        JournalEntryDTO dto1 = JournalEntryDTO.builder().date(today).description("Today's entry").build();
        JournalEntryDTO dto2 = JournalEntryDTO.builder().date(yesterday).description("Yesterday's entry").build();

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(journalEntryRepository.findAllByUser(user)).thenReturn(List.of(entry1, entry2));
        when(mapper.mapList(List.of(entry1, entry2), JournalEntryDTO.class)).thenReturn(List.of(dto1, dto2));

        List<JournalEntryDTO> result = journalEntryService.findAll();

        assertThat(result).containsExactly(dto1, dto2);
    }

    @Test
    void create_shouldCreateJournalEntry() {
        LocalDate today = LocalDate.now();
        JournalEntryDTO dto = JournalEntryDTO.builder().date(today).description("New entry").build();

        JournalEntry toSave = JournalEntry.builder().date(today).description("New entry").build();
        JournalEntry saved = JournalEntry.builder().id(1L).date(today).description("New entry").build();
        JournalEntryDTO resultDTO = JournalEntryDTO.builder().date(today).description("New entry").build();

        when(mapper.map(dto, JournalEntry.class)).thenReturn(toSave);
        when(journalEntryRepository.save(toSave)).thenReturn(saved);
        when(mapper.map(saved, JournalEntryDTO.class)).thenReturn(resultDTO);

        JournalEntryDTO result = journalEntryService.create(dto);

        assertThat(result).isEqualTo(resultDTO);
    }

    @Test
    void findByDate_shouldReturnJournalEntry() {
        User user = new User();
        LocalDate today = LocalDate.now();
        JournalEntry entry = JournalEntry.builder().id(1L).date(today).description("Today").build();
        JournalEntryDTO dto = JournalEntryDTO.builder().date(today).description("Today").build();

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(journalEntryRepository.findByUserAndDate(user, today)).thenReturn(Optional.of(entry));
        when(mapper.map(entry, JournalEntryDTO.class)).thenReturn(dto);

        JournalEntryDTO result = journalEntryService.findByDate(today);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void delete_shouldDeleteJournalEntry() {
        User user = new User();
        LocalDate today = LocalDate.now();
        JournalEntry entry = JournalEntry.builder().id(1L).date(today).description("Entry").build();

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(journalEntryRepository.findByUserAndDate(user, today)).thenReturn(Optional.of(entry));

        journalEntryService.delete(today);

        verify(journalEntryRepository).deleteById(1L);
    }

    @Test
    void update_shouldUpdateJournalEntry() {
        User user = new User();
        LocalDate today = LocalDate.now();

        JournalEntryDTO dto = JournalEntryDTO.builder().date(today).description("Updated entry").build();
        JournalEntry existing = JournalEntry.builder().id(1L).date(today).description("Old").build();
        JournalEntry updated = JournalEntry.builder().id(1L).date(today).description("Updated entry").build();
        JournalEntryDTO resultDTO = JournalEntryDTO.builder().date(today).description("Updated entry").build();

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(journalEntryRepository.findByUserAndDate(user, today)).thenReturn(Optional.of(existing));
        when(journalEntryRepository.save(existing)).thenReturn(updated);
        when(mapper.map(updated, JournalEntryDTO.class)).thenReturn(resultDTO);

        JournalEntryDTO result = journalEntryService.update(dto, today);

        assertThat(result).isEqualTo(resultDTO);
    }

    @Test
    void update_shouldThrowException_whenJournalEntryNotFound() {
        User user = new User();
        LocalDate today = LocalDate.now();
        JournalEntryDTO dto = JournalEntryDTO.builder().date(today).description("Update").build();

        when(userSecurity.getCurrentUser()).thenReturn(user);
        when(journalEntryRepository.findByUserAndDate(user, today)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> journalEntryService.update(dto, today))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.JOURNAL_ENTRY_NOT_FOUND.getMessage());
    }
}
