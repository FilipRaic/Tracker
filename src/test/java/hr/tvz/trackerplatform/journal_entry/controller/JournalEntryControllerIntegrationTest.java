package hr.tvz.trackerplatform.journal_entry.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.journal_entry.dto.JournalEntryDTO;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JournalEntryControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/journal";

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Test
    void findAllJournalEntry_shouldReturnJournalEntries() throws Exception {
        JournalEntry entry1 = JournalEntry.builder()
                .user(user)
                .date(LocalDate.now())
                .description("Today's entry")
                .build();

        JournalEntry entry2 = JournalEntry.builder()
                .user(user)
                .date(LocalDate.now().minusDays(1))
                .description("Yesterday's entry")
                .build();

        journalEntryRepository.save(entry1);
        journalEntryRepository.save(entry2);

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<JournalEntryDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        JournalEntryDTO.builder()
                                .date(LocalDate.now())
                                .description("Today's entry")
                                .build(),
                        JournalEntryDTO.builder()
                                .date(LocalDate.now().minusDays(1))
                                .description("Yesterday's entry")
                                .build()
                ));
    }

    @Test
    void findAllJournalEntry_shouldReturnEmptyList_whenNoEntries() throws Exception {
        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<JournalEntryDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEmpty();
    }

    @Test
    void findJournalEntryByDate_shouldReturnJournalEntry() throws Exception {
        LocalDate date = LocalDate.now();
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .date(date)
                .description("Today's entry")
                .build();

        journalEntryRepository.save(entry);

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/" + date)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        JournalEntryDTO actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(JournalEntryDTO.builder()
                        .date(date)
                        .description("Today's entry")
                        .build());
    }

    @Test
    void findJournalEntryByDate_shouldReturnNotFound_whenEntryDoesNotExist() throws Exception {
        LocalDate date = LocalDate.now();

        mockMvc.perform(withJwt(get(BASE_URL + "/" + date)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createJournalEntry_shouldCreateSuccessfully() throws Exception {
        JournalEntryDTO createDTO = JournalEntryDTO.builder()
                .date(LocalDate.now())
                .description("New entry")
                .build();

        var response = mockMvc.perform(withJwt(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createDTO))))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        JournalEntryDTO actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        JournalEntry savedEntry = journalEntryRepository.findByUserAndDate(user, LocalDate.now()).orElseThrow();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(JournalEntryDTO.builder()
                        .date(LocalDate.now())
                        .description("New entry")
                        .build());
        assertThat(savedEntry.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void createJournalEntry_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        JournalEntryDTO invalidDTO = JournalEntryDTO.builder()
                .date(null)
                .description("Invalid entry")
                .build();

        mockMvc.perform(withJwt(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDTO))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteByDate_shouldDeleteSuccessfully() throws Exception {
        LocalDate date = LocalDate.now();
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .date(date)
                .description("Entry to delete")
                .build();

        journalEntryRepository.save(entry);

        mockMvc.perform(withJwt(delete(BASE_URL + "/" + date)))
                .andExpect(status().isNoContent());

        assertThat(journalEntryRepository.findByUserAndDate(user, date)).isEmpty();
    }

    @Test
    void deleteByDate_shouldReturnNotFound_whenEntryDoesNotExist() throws Exception {
        LocalDate date = LocalDate.now();

        mockMvc.perform(withJwt(delete(BASE_URL + "/" + date)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateJournalEntry_shouldUpdateSuccessfully() throws Exception {
        LocalDate date = LocalDate.now();
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .date(date)
                .description("Original entry")
                .build();

        journalEntryRepository.save(entry);

        JournalEntryDTO updateDTO = JournalEntryDTO.builder()
                .date(date)
                .description("Updated entry")
                .build();

        var response = mockMvc.perform(withJwt(put(BASE_URL + "/" + date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO))))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        JournalEntryDTO actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        JournalEntry updatedEntry = journalEntryRepository.findByUserAndDate(user, date).orElseThrow();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(JournalEntryDTO.builder()
                        .date(date)
                        .description("Updated entry")
                        .build());
        assertThat(updatedEntry.getDescription()).isEqualTo("Updated entry");
        assertThat(updatedEntry.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void updateJournalEntry_shouldReturnNotFound_whenEntryDoesNotExist() throws Exception {
        LocalDate date = LocalDate.now();
        JournalEntryDTO updateDTO = JournalEntryDTO.builder()
                .date(date)
                .description("Updated entry")
                .build();

        mockMvc.perform(withJwt(put(BASE_URL + "/" + date)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDTO))))
                .andExpect(status().isNotFound());
    }
}
