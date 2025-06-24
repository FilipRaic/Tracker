package hr.tvz.trackerplatform.habit.model;

import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class HabitTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        Habit h1 = createHabit(100L, "Test", LocalDate.now(), "desc", freq, user);
        Habit h2 = createHabit(100L, "Other", LocalDate.now().minusDays(1), "other", freq, user);

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        Habit h1 = createHabit(1L, "Test", LocalDate.now(), "desc", freq, user);
        Habit h2 = createHabit(2L, "Test", LocalDate.now(), "desc", freq, user);

        assertThat(h1).isNotEqualTo(h2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        LocalDate now = LocalDate.now();
        Habit h1 = createHabit(null, "Test", now, "desc", freq, user);
        Habit h2 = createHabit(null, "Test", now, "desc", freq, user);

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        LocalDate now = LocalDate.now();
        Habit h1 = createHabit(null, "Test", now, "desc", freq, user);
        Habit h2 = createHabit(null, "Different", now, "desc", freq, user);

        assertThat(h1).isNotEqualTo(h2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        Habit habit = createHabit(1L, "Test", LocalDate.now(), "desc",
                createHabitFrequency(1, "DAILY"), createUser(1L, "user"));

        assertThat(habit.equals("not a habit")).isFalse();
    }

    @Test
    void testHashCode_consistency() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        LocalDate date = LocalDate.now();
        Habit h1 = createHabit(null, "Test", date, "desc", freq, user);
        Habit h2 = createHabit(null, "Test", date, "desc", freq, user);

        assertThat(h1.hashCode()).isEqualTo(h2.hashCode());
    }

    @Test
    void testToString_containsFields() {
        Habit habit = createHabit(1L, "Exercise", LocalDate.of(2024, 1, 1),
                "Morning run", createHabitFrequency(1, "DAILY"), createUser(1L, "user"));
        String toString = habit.toString();

        assertThat(toString)
                .contains("Exercise")
                .contains("Morning run");
    }

    @Test
    void testGettersSetters() {
        Habit habit = new Habit();
        habit.setName("Reading");
        habit.setBegin(LocalDate.of(2023, 5, 20));
        habit.setDescription("20 min");
        habit.setUser(createUser(2L, "user2@mail.com"));
        habit.setHabitFrequency(createHabitFrequency(3, "WEEKLY"));

        assertThat(habit.getName()).isEqualTo("Reading");
        assertThat(habit.getBegin()).isEqualTo(LocalDate.of(2023, 5, 20));
        assertThat(habit.getDescription()).isEqualTo("20 min");
        assertThat(habit.getUser().getId()).isEqualTo(2L);
        assertThat(habit.getHabitFrequency().getName()).isEqualTo("WEEKLY");
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        Habit habit = createHabit(1L, "Test", LocalDate.now(), "desc", freq, user);

        assertThat(habit).isEqualTo(habit);
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        Habit habit = createHabit(1L, "Test", LocalDate.now(), "desc",
                createHabitFrequency(1, "DAILY"), createUser(1L, "user"));

        assertThat(habit.equals(null)).isFalse();
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        Habit h1 = createHabit(null, null, null, null, null, null);
        Habit h2 = createHabit(null, null, null, null, null, null);

        assertThat(h1).isEqualTo(h2);
    }

    @Test
    void testEquals_nullUser_shouldNotBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        LocalDate now = LocalDate.now();

        Habit h1 = createHabit(null, "Test", now, "desc", freq, null);
        Habit h2 = createHabit(null, "Test", now, "desc", freq, createUser(1L, "user"));

        assertThat(h1).isNotEqualTo(h2);
    }

    @Test
    void testEquals_differentFrequencies_shouldNotBeEqual() {
        User user = createUser(1L, "user");
        LocalDate now = LocalDate.now();

        HabitFrequency freq1 = createHabitFrequency(1, "DAILY");
        HabitFrequency freq2 = createHabitFrequency(2, "WEEKLY");

        Habit h1 = createHabit(null, "Test", now, "desc", freq1, user);
        Habit h2 = createHabit(null, "Test", now, "desc", freq2, user);

        assertThat(h1).isNotEqualTo(h2);
    }

    @Test
    void testEquals_differentBeginDates_shouldNotBeEqual() {
        HabitFrequency freq = createHabitFrequency(1, "DAILY");
        User user = createUser(1L, "user");

        Habit h1 = createHabit(null, "Test", LocalDate.of(2024, 1, 1), "desc", freq, user);
        Habit h2 = createHabit(null, "Test", LocalDate.of(2023, 1, 1), "desc", freq, user);

        assertThat(h1).isNotEqualTo(h2);
    }

    private HabitFrequency createHabitFrequency(Integer id, String name) {
        return HabitFrequency.builder().id(id).name(name).build();
    }

    private User createUser(Long id, String email) {
        return new User(id, "Test", "User", email, Role.USER, "TestPassword");
    }

    private Habit createHabit(Long id, String name, LocalDate begin, String desc, HabitFrequency freq, User user) {
        return Habit.builder()
                .id(id)
                .name(name)
                .begin(begin)
                .description(desc)
                .habitFrequency(freq)
                .user(user)
                .build();
    }
}
