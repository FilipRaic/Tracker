package hr.tvz.trackerplatform.habit.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HabitFrequencyTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        HabitFrequency f1 = createHabitFrequency(1, "DAILY");
        HabitFrequency f2 = createHabitFrequency(1, "WEEKLY");

        assertThat(f1).isEqualTo(f2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        HabitFrequency f1 = createHabitFrequency(1, "DAILY");
        HabitFrequency f2 = createHabitFrequency(2, "DAILY");

        assertThat(f1).isNotEqualTo(f2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        HabitFrequency f1 = createHabitFrequency(null, "DAILY");
        HabitFrequency f2 = createHabitFrequency(null, "DAILY");

        assertThat(f1).isEqualTo(f2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        HabitFrequency f1 = createHabitFrequency(null, "DAILY");
        HabitFrequency f2 = createHabitFrequency(null, "WEEKLY");

        assertThat(f1).isNotEqualTo(f2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        HabitFrequency frequency = createHabitFrequency(1, "DAILY");

        assertThat(frequency.equals("not a HabitFrequency")).isFalse();
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        HabitFrequency frequency = createHabitFrequency(1, "DAILY");

        assertThat(frequency.equals(null)).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        HabitFrequency frequency = createHabitFrequency(1, "DAILY");

        assertThat(frequency).isEqualTo(frequency);
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        HabitFrequency f1 = createHabitFrequency(null, null);
        HabitFrequency f2 = createHabitFrequency(null, null);

        assertThat(f1).isEqualTo(f2);
    }

    @Test
    void testHashCode_consistency() {
        HabitFrequency f1 = createHabitFrequency(null, "DAILY");
        HabitFrequency f2 = createHabitFrequency(null, "DAILY");

        assertThat(f1.hashCode()).isEqualTo(f2.hashCode());
    }

    @Test
    void testToString_containsFields() {
        HabitFrequency frequency = createHabitFrequency(1, "MONTHLY");

        String str = frequency.toString();

        assertThat(str)
                .contains("MONTHLY")
                .contains("1");
    }

    @Test
    void testLombokMethods_gettersSetters() {
        HabitFrequency freq = new HabitFrequency();
        freq.setId(42);
        freq.setName("YEARLY");

        assertThat(freq.getId()).isEqualTo(42);
        assertThat(freq.getName()).isEqualTo("YEARLY");
    }

    private HabitFrequency createHabitFrequency(Integer id, String name) {
        return HabitFrequency.builder()
                .id(id)
                .name(name)
                .build();
    }
}
