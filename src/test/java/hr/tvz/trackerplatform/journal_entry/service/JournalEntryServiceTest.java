package hr.tvz.trackerplatform.journal_entry.service;

import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryServiceImpl journalEntryService;

    @Test
    void findAll_shouldReturnAllJournalEntries() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        JournalEntry entry1 = JournalEntry.builder()
                .id(1L)
                .date(today)
                .description("Today's entry")
                .build();

        JournalEntry entry2 = JournalEntry.builder()
                .id(2L)
                .date(yesterday)
                .description("Yesterday's entry")
                .build();

        when(journalEntryRepository.findAll()).thenReturn(List.of(entry1, entry2));

        List<JournalEntryDTO> result = journalEntryService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDate()).isEqualTo(today);
        assertThat(result.get(0).getDescription()).isEqualTo("Today's entry");
        assertThat(result.get(1).getDate()).isEqualTo(yesterday);
        assertThat(result.get(1).getDescription()).isEqualTo("Yesterday's entry");
    }

    @Test
    void create_shouldCreateJournalEntry() {
        LocalDate today = LocalDate.now();
        JournalEntryDTO dto = JournalEntryDTO.builder()
                .date(today)
                .description("New entry")
                .build();

        JournalEntry savedEntry = JournalEntry.builder()
                .id(1L)
                .date(today)
                .description("New entry")
                .build();

        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(savedEntry);

        JournalEntryDTO result = journalEntryService.create(dto);

        ArgumentCaptor<JournalEntry> entryCaptor = ArgumentCaptor.forClass(JournalEntry.class);
        verify(journalEntryRepository).save(entryCaptor.capture());

        JournalEntry capturedEntry = entryCaptor.getValue();
        assertThat(capturedEntry.getDate()).isEqualTo(today);
        assertThat(capturedEntry.getDescription()).isEqualTo("New entry");

        assertThat(result.getDate()).isEqualTo(today);
        assertThat(result.getDescription()).isEqualTo("New entry");
    }

    @Test
    void findByDate_shouldReturnJournalEntry() {
        LocalDate today = LocalDate.now();
        JournalEntry entry = JournalEntry.builder()
                .id(1L)
                .date(today)
                .description("Today's entry")
                .build();

        when(journalEntryRepository.findByDate(today)).thenReturn(entry);

        JournalEntryDTO result = journalEntryService.findByDate(today);

        assertThat(result.getDate()).isEqualTo(today);
        assertThat(result.getDescription()).isEqualTo("Today's entry");
    }

    @Test
    void delete_shouldDeleteJournalEntry() {
        LocalDate today = LocalDate.now();
        JournalEntry entry = JournalEntry.builder()
                .id(1L)
                .date(today)
                .description("Today's entry")
                .build();

        when(journalEntryRepository.findByDate(today)).thenReturn(entry);

        journalEntryService.delete(today);

        verify(journalEntryRepository).deleteById(1L);
    }

    @Test
    void update_shouldUpdateJournalEntry() {
        LocalDate today = LocalDate.now();
        JournalEntryDTO dto = JournalEntryDTO.builder()
                .date(today)
                .description("Updated entry")
                .build();

        JournalEntry existingEntry = JournalEntry.builder()
                .id(1L)
                .date(today)
                .description("Original entry")
                .build();

        when(journalEntryRepository.findByDate(today)).thenReturn(existingEntry);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(existingEntry);

        JournalEntryDTO result = journalEntryService.update(dto, today);

        ArgumentCaptor<JournalEntry> entryCaptor = ArgumentCaptor.forClass(JournalEntry.class);
        verify(journalEntryRepository).save(entryCaptor.capture());

        JournalEntry capturedEntry = entryCaptor.getValue();
        assertThat(capturedEntry.getId()).isEqualTo(1L);
        assertThat(capturedEntry.getDate()).isEqualTo(today);
        assertThat(capturedEntry.getDescription()).isEqualTo("Updated entry");

        assertThat(result.getDate()).isEqualTo(today);
        assertThat(result.getDescription()).isEqualTo("Updated entry");
    }

    @Test
    void update_shouldThrowException_whenJournalEntryNotFound() {
        LocalDate today = LocalDate.now();
        JournalEntryDTO dto = JournalEntryDTO.builder()
                .date(today)
                .description("Updated entry")
                .build();

        when(journalEntryRepository.findByDate(today)).thenReturn(null);

        assertThatThrownBy(() -> journalEntryService.update(dto, today))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Journal entry not found.");
    }
}
