package hr.tvz.trackerplatform.habit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hr.tvz.trackerplatform.habit.dto.HabitDTO;
import hr.tvz.trackerplatform.habit.dto.HabitStatusDTO;
import hr.tvz.trackerplatform.habit.service.HabitService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HabitControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitController habitController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(habitController).build();
    }

    @Test
    void findAllHabits_shouldReturnAllHabits() throws Exception {
        LocalDate today = LocalDate.now();
        HabitDTO habit1 = new HabitDTO(1L, "Run", today, "day", "Morning run");
        HabitDTO habit2 = new HabitDTO(2L, "Read", today, "week", "Book reading");

        when(habitService.findAll()).thenReturn(List.of(habit1, habit2));

        mockMvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Run"))
                .andExpect(jsonPath("$[1].name").value("Read"));
    }

    @Test
    void findCurrentHabitsWithStatus_shouldReturnHabitsWithStatus() throws Exception {
        LocalDate today = LocalDate.now();
        HabitStatusDTO status1 = HabitStatusDTO.builder()
                .id(1L)
                .name("Run")
                .startDate(today)
                .frequency("day")
                .notes("Morning run")
                .dueDate(today)
                .done(false)
                .build();

        HabitStatusDTO status2 = HabitStatusDTO.builder()
                .id(2L)
                .name("Read")
                .startDate(today)
                .frequency("week")
                .notes("Book reading")
                .dueDate(today.plusDays(7))
                .done(true)
                .build();

        when(habitService.findCurrentHabitsWithStatus()).thenReturn(List.of(status1, status2));

        mockMvc.perform(get("/api/habits/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Run"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[1].name").value("Read"))
                .andExpect(jsonPath("$[1].done").value(true));
    }

    @Test
    void createHabit_shouldCreateAndReturnHabit() throws Exception {
        LocalDate today = LocalDate.now();
        HabitDTO habitToCreate = new HabitDTO(null, "Meditate", today, "day", "Morning meditation");
        HabitDTO createdHabit = new HabitDTO(1L, "Meditate", today, "day", "Morning meditation");

        when(habitService.create(any(HabitDTO.class))).thenReturn(createdHabit);

        mockMvc.perform(post("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(habitToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Meditate"))
                .andExpect(jsonPath("$.frequency").value("day"));
    }

    @Test
    void changeHabitStatus_shouldChangeAndReturnStatus() throws Exception {
        Long habitId = 1L;
        LocalDate today = LocalDate.now();
        HabitStatusDTO updatedStatus = HabitStatusDTO.builder()
                .id(habitId)
                .name("Run")
                .startDate(today)
                .frequency("day")
                .notes("Morning run")
                .dueDate(today)
                .done(true)
                .build();

        when(habitService.changeHabitStatus(habitId)).thenReturn(updatedStatus);

        mockMvc.perform(put("/api/habits/status/{habitId}", habitId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(habitId))
                .andExpect(jsonPath("$.name").value("Run"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void deleteHabit_shouldDeleteAndReturnNoContent() throws Exception {
        Long habitId = 1L;
        doNothing().when(habitService).deleteHabit(habitId);

        mockMvc.perform(delete("/api/habits/{habitId}", habitId))
                .andExpect(status().isNoContent());

        verify(habitService).deleteHabit(habitId);
    }
}
