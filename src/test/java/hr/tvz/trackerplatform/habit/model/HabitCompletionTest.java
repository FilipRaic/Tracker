package hr.tvz.trackerplatform.habit.model;

import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class HabitCompletionTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        Habit habit = createHabit(1L, "Hydrate");

        HabitCompletion hc1 = createHabitCompletion(100L, LocalDate.now(), true, habit);
        HabitCompletion hc2 = createHabitCompletion(100L, LocalDate.now().minusDays(1), false, habit);

        assertThat(hc1).isEqualTo(hc2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        Habit habit = createHabit(1L, "Hydrate");

        HabitCompletion hc1 = createHabitCompletion(1L, LocalDate.now(), true, habit);
        HabitCompletion hc2 = createHabitCompletion(2L, LocalDate.now(), true, habit);

        assertThat(hc1).isNotEqualTo(hc2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        Habit habit = createHabit(1L, "Hydrate");
        LocalDate date = LocalDate.of(2024, 6, 1);

        HabitCompletion hc1 = createHabitCompletion(null, date, true, habit);
        HabitCompletion hc2 = createHabitCompletion(null, date, true, habit);

        assertThat(hc1).isEqualTo(hc2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        Habit habit = createHabit(1L, "Hydrate");
        LocalDate date = LocalDate.of(2024, 6, 1);

        HabitCompletion hc1 = createHabitCompletion(null, date, true, habit);
        HabitCompletion hc2 = createHabitCompletion(null, date, false, habit);

        assertThat(hc1).isNotEqualTo(hc2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        HabitCompletion hc = createHabitCompletion(1L, LocalDate.now(), true, createHabit(1L, "Sleep"));

        assertThat(hc.equals("not a HabitCompletion")).isFalse();
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        HabitCompletion hc = createHabitCompletion(1L, LocalDate.now(), true, createHabit(1L, "Stretch"));

        assertThat(hc.equals(null)).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        HabitCompletion hc = createHabitCompletion(1L, LocalDate.now(), true, createHabit(1L, "Yoga"));

        assertThat(hc).isEqualTo(hc);
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        HabitCompletion hc1 = createHabitCompletion(null, null, null, null);
        HabitCompletion hc2 = createHabitCompletion(null, null, null, null);

        assertThat(hc1).isEqualTo(hc2);
    }

    @Test
    void testEquals_differentHabit_shouldNotBeEqual() {
        HabitCompletion hc1 = createHabitCompletion(null, LocalDate.now(), true, createHabit(1L, "Read"));
        HabitCompletion hc2 = createHabitCompletion(null, LocalDate.now(), true, createHabit(2L, "Exercise"));

        assertThat(hc1).isNotEqualTo(hc2);
    }

    @Test
    void testHashCode_consistency() {
        Habit habit = createHabit(1L, "Hydrate");
        LocalDate date = LocalDate.of(2024, 6, 1);

        HabitCompletion hc1 = createHabitCompletion(null, date, true, habit);
        HabitCompletion hc2 = createHabitCompletion(null, date, true, habit);

        assertThat(hc1.hashCode()).isEqualTo(hc2.hashCode());
    }

    @Test
    void testToString_containsFields() {
        Habit habit = createHabit(1L, "Meditate");
        HabitCompletion hc = createHabitCompletion(5L, LocalDate.of(2024, 3, 14), true, habit);

        String toString = hc.toString();

        assertThat(toString)
                .contains("2024-03-14")
                .contains("true")
                .contains("Meditate");
    }

    @Test
    void testLombokMethods_gettersSetters() {
        Habit habit = createHabit(2L, "Workout");

        HabitCompletion hc = new HabitCompletion();
        hc.setId(10L);
        hc.setCompletionDate(LocalDate.of(2024, 8, 10));
        hc.setDone(false);
        hc.setHabit(habit);

        assertThat(hc.getId()).isEqualTo(10L);
        assertThat(hc.getCompletionDate()).isEqualTo(LocalDate.of(2024, 8, 10));
        assertThat(hc.getDone()).isFalse();
        assertThat(hc.getHabit().getName()).isEqualTo("Workout");
    }

    private HabitCompletion createHabitCompletion(Long id, LocalDate date, Boolean done, Habit habit) {
        return HabitCompletion.builder()
                .id(id)
                .completionDate(date)
                .done(done)
                .habit(habit)
                .build();
    }

    private Habit createHabit(Long id, String name) {
        return Habit.builder()
                .id(id)
                .name(name)
                .begin(LocalDate.of(2024, 1, 1))
                .description("Sample")
                .habitFrequency(HabitFrequency.builder().id(1).name("DAILY").build())
                .user(new User(1L, "John", "Doe", "john@example.com", Role.USER, "password"))
                .build();
    }
}
