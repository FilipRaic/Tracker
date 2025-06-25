package hr.tvz.trackerplatform.journal_entry.model;

import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class JournalEntryTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        User user = createUser(1L, "user@example.com");
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(100L, "Entry", date, user);
        JournalEntry entry2 = createJournalEntry(100L, "Different", date.plusDays(1), user);

        assertThat(entry1).isEqualTo(entry2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(1L, "Entry", date, user);
        JournalEntry entry2 = createJournalEntry(2L, "Entry", date, user);

        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        User user = createUser(1L, "user@example.com");
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(null, "Entry", date, user);
        JournalEntry entry2 = createJournalEntry(null, "Entry", date, user);

        assertThat(entry1).isEqualTo(entry2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(null, "Entry", date, user);
        JournalEntry entry2 = createJournalEntry(null, "Different", date, user);

        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        JournalEntry entry = createJournalEntry(1L, "Entry", LocalDate.now(), createUser(1L, "user@example.com"));

        assertThat(entry.equals("not a journal entry")).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        JournalEntry entry = createJournalEntry(1L, "Entry", LocalDate.now(), createUser(1L, "user@example.com"));

        assertThat(entry).isEqualTo(entry);
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        JournalEntry entry = createJournalEntry(1L, "Entry", LocalDate.now(), createUser(1L, "user@example.com"));

        assertThat(entry.equals(null)).isFalse();
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        JournalEntry entry1 = createJournalEntry(null, null, null, null);
        JournalEntry entry2 = createJournalEntry(null, null, null, null);

        assertThat(entry1).isEqualTo(entry2);
    }

    @Test
    void testEquals_nullUser_shouldNotBeEqual() {
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(null, "Entry", date, null);
        JournalEntry entry2 = createJournalEntry(null, "Entry", date, createUser(1L, "user@example.com"));

        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testEquals_differentDates_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");

        JournalEntry entry1 = createJournalEntry(null, "Entry", LocalDate.of(2024, 1, 1), user);
        JournalEntry entry2 = createJournalEntry(null, "Entry", LocalDate.of(2023, 1, 1), user);

        assertThat(entry1).isNotEqualTo(entry2);
    }

    @Test
    void testHashCode_consistency() {
        User user = createUser(1L, "user@example.com");
        LocalDate date = LocalDate.now();

        JournalEntry entry1 = createJournalEntry(null, "Entry", date, user);
        JournalEntry entry2 = createJournalEntry(null, "Entry", date, user);

        assertThat(entry1).hasSameHashCodeAs(entry2);
    }

    @Test
    void testToString_containsFields() {
        JournalEntry entry = createJournalEntry(1L, "Morning reflection", LocalDate.of(2024, 1, 1), createUser(1L, "user@example.com"));
        String toString = entry.toString();

        assertThat(toString)
                .contains("Morning reflection")
                .contains("2024-01-01");
    }

    @Test
    void testGettersSetters() {
        JournalEntry entry = new JournalEntry();
        entry.setId(1L);
        entry.setDescription("Evening journal");
        entry.setDate(LocalDate.of(2023, 5, 20));
        entry.setUser(createUser(2L, "user2@example.com"));

        assertThat(entry.getId()).isEqualTo(1L);
        assertThat(entry.getDescription()).isEqualTo("Evening journal");
        assertThat(entry.getDate()).isEqualTo(LocalDate.of(2023, 5, 20));
        assertThat(entry.getUser().getId()).isEqualTo(2L);
    }

    private User createUser(Long id, String email) {
        return new User(id, "Test", "User", email, Role.USER, "TestPassword");
    }

    private JournalEntry createJournalEntry(Long id, String description, LocalDate date, User user) {
        return JournalEntry.builder()
                .id(id)
                .description(description)
                .date(date)
                .user(user)
                .build();
    }
}
