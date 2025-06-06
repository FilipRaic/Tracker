package hr.tvz.trackerplatform.journal_entry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.service.JournalEntryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JournalEntryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private JournalEntryService journalEntryService;

    @InjectMocks
    private JournalEntryController journalEntryController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(journalEntryController).build();
    }

    @Test
    void findAllJournalEntry_shouldReturnAllEntries() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        JournalEntryDTO entry1 = JournalEntryDTO.builder()
                .date(today)
                .description("Today's entry")
                .build();

        JournalEntryDTO entry2 = JournalEntryDTO.builder()
                .date(yesterday)
                .description("Yesterday's entry")
                .build();

        when(journalEntryService.findAll()).thenReturn(List.of(entry1, entry2));

        mockMvc.perform(get("/api/journal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Today's entry"))
                .andExpect(jsonPath("$[1].description").value("Yesterday's entry"));
    }

    @Test
    void findJournalEntryByDate_shouldReturnEntry() throws Exception {
        LocalDate date = LocalDate.of(2023, 5, 15);
        JournalEntryDTO entry = JournalEntryDTO.builder()
                .date(date)
                .description("Test entry")
                .build();

        when(journalEntryService.findByDate(date)).thenReturn(entry);

        mockMvc.perform(get("/api/journal/{date}", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.description").value("Test entry"));
    }

    @Test
    void createJournalEntry_shouldCreateAndReturnEntry() throws Exception {
        LocalDate date = LocalDate.of(2023, 5, 15);
        JournalEntryDTO entryToCreate = JournalEntryDTO.builder()
                .date(date)
                .description("New entry")
                .build();

        JournalEntryDTO createdEntry = JournalEntryDTO.builder()
                .date(date)
                .description("New entry")
                .build();

        when(journalEntryService.create(any(JournalEntryDTO.class))).thenReturn(createdEntry);

        mockMvc.perform(post("/api/journal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.description").value("New entry"));
    }

    @Test
    void deleteByDate_shouldDeleteAndReturnNoContent() throws Exception {
        LocalDate date = LocalDate.of(2023, 5, 15);
        doNothing().when(journalEntryService).delete(date);

        mockMvc.perform(delete("/api/journal/{date}", date))
                .andExpect(status().isNoContent());

        verify(journalEntryService).delete(date);
    }

    @Test
    void deleteByDate_shouldReturnNotFound_whenEntryDoesNotExist() throws Exception {
        LocalDate date = LocalDate.of(2023, 5, 15);
        doThrow(new EntityNotFoundException()).when(journalEntryService).delete(date);

        mockMvc.perform(delete("/api/journal/{date}", date))
                .andExpect(status().isNotFound());

        verify(journalEntryService).delete(date);
    }

    @Test
    void updateJournalEntry_shouldUpdateAndReturnEntry() throws Exception {
        LocalDate date = LocalDate.of(2023, 5, 15);
        JournalEntryDTO entryToUpdate = JournalEntryDTO.builder()
                .date(date)
                .description("Updated entry")
                .build();

        JournalEntryDTO updatedEntry = JournalEntryDTO.builder()
                .date(date)
                .description("Updated entry")
                .build();

        when(journalEntryService.update(any(JournalEntryDTO.class), eq(date))).thenReturn(updatedEntry);

        mockMvc.perform(put("/api/journal/{date}", date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.description").value("Updated entry"));
    }
}
