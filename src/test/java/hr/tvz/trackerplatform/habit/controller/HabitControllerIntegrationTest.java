package hr.tvz.trackerplatform.habit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HabitControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/habits";

    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitFrequencyRepository habitFrequencyRepository;
    @Autowired
    private HabitCompletionRepository habitCompletionRepository;

    @Test
    void findAllHabits_shouldReturnAllUserHabits() throws Exception {
        HabitFrequency frequency = habitFrequencyRepository.save(HabitFrequency.builder().name("day").build());

        Habit habit = habitRepository.save(Habit.builder()
                .name("Drink Water")
                .description("Drink 8 glasses")
                .begin(LocalDate.now())
                .habitFrequency(frequency)
                .user(user)
                .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<HabitDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .singleElement()
                .isEqualTo(HabitDTO.builder()
                        .id(habit.getId())
                        .name("Drink Water")
                        .startDate(LocalDate.now())
                        .frequency(frequency.getName())
                        .notes("Drink 8 glasses")
                        .build());
    }

    @Test
    void findCurrentHabitsWithStatus_shouldReturnHabitStatuses() throws Exception {
        HabitFrequency frequency = habitFrequencyRepository.save(HabitFrequency.builder().name("day").build());

        Habit habit = habitRepository.save(Habit.builder()
                .name("Read Book")
                .description("30 mins daily")
                .begin(LocalDate.now())
                .habitFrequency(frequency)
                .user(user)
                .build());

        var response = mockMvc.perform(withJwt(get(BASE_URL + "/status")))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<HabitStatusDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .singleElement()
                .isEqualTo(HabitStatusDTO.builder()
                        .done(false)
                        .dueDate(LocalDate.now().plusDays(1))
                        .id(habit.getId())
                        .name("Read Book")
                        .startDate(LocalDate.now())
                        .frequency(frequency.getName())
                        .notes("30 mins daily")
                        .build());
    }

    @Test
    void createHabit_shouldPersistHabit() throws Exception {
        HabitFrequency frequency = habitFrequencyRepository.save(HabitFrequency.builder().name("day").build());

        HabitDTO habitDTO = HabitDTO.builder()
                .name("Meditation")
                .startDate(LocalDate.now())
                .frequency(frequency.getName())
                .build();

        var response = mockMvc.perform(withJwt(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(habitDTO))))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        HabitDTO created = mapper.readValue(response.getContentAsString(), HabitDTO.class);
        assertThat(created.getName()).isEqualTo("Meditation");

        Habit actual = habitRepository.findById(created.getId()).orElseThrow();
        assertThat(actual.getName()).isEqualTo("Meditation");
    }

    @Test
    void changeHabitStatus_shouldToggleStatus() throws Exception {
        HabitFrequency frequency = habitFrequencyRepository.save(HabitFrequency.builder().name("day").build());

        Habit habit = habitRepository.save(Habit.builder()
                .name("Exercise")
                .description("Jogging")
                .begin(LocalDate.now())
                .habitFrequency(frequency)
                .user(user)
                .build());

        habitCompletionRepository.save(HabitCompletion.builder()
                .completionDate(LocalDate.now())
                .done(false)
                .habit(habit)
                .streak(0)
                .build());

        var response = mockMvc.perform(withJwt(put(BASE_URL + "/status/" + habit.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        HabitStatusDTO updatedStatus = mapper.readValue(response.getContentAsString(), HabitStatusDTO.class);

        assertThat(updatedStatus.getId()).isEqualTo(habit.getId());
        assertThat(updatedStatus.isDone()).isTrue();
    }

    @Test
    void deleteHabit_shouldRemoveHabit() throws Exception {
        HabitFrequency frequency = habitFrequencyRepository.save(HabitFrequency.builder().name("day").build());

        Habit habit = habitRepository.save(Habit.builder()
                .name("Stretching")
                .description("Morning routine")
                .begin(LocalDate.now())
                .habitFrequency(frequency)
                .user(user)
                .build());

        mockMvc.perform(withJwt(delete(BASE_URL + "/" + habit.getId())))
                .andExpect(status().isNoContent());

        assertThat(habitRepository.findById(habit.getId())).isEmpty();
    }
}
