package hr.tvz.trackerplatform.achievement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AchievementTest {

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();
    }

    @Test
    void testDefaultConstructor() {
        Achievement ach = new Achievement();
        assertNull(ach.getId());
        assertNull(ach.getName());
        assertNull(ach.getUnlockCondition());
        assertNull(ach.getEmoji());
        assertNull(ach.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Marathon Runner";
        String unlockCondition = "Run 10km in a week";
        String emoji = "🏃";
        String description = "Awarded for running 10 kilometers in a single week.";

        Achievement ach = new Achievement(id, name, unlockCondition, emoji, description);

        assertEquals(id, ach.getId());
        assertEquals(name, ach.getName());
        assertEquals(unlockCondition, ach.getUnlockCondition());
        assertEquals(emoji, ach.getEmoji());
        assertEquals(description, ach.getDescription());
    }

    @Test
    void testBuilder() {
        Long id = 2L;
        String name = "Healthy Eater";
        String unlockCondition = "Log 7 healthy meals";
        String emoji = "🥗";
        String description = "Awarded for logging 7 healthy meals in a row.";

        Achievement ach = Achievement.builder()
                .id(id)
                .name(name)
                .unlockCondition(unlockCondition)
                .emoji(emoji)
                .description(description)
                .build();

        assertEquals(id, ach.getId());
        assertEquals(name, ach.getName());
        assertEquals(unlockCondition, ach.getUnlockCondition());
        assertEquals(emoji, ach.getEmoji());
        assertEquals(description, ach.getDescription());
    }

    @Test
    void testGettersAndSetters() {
        Achievement ach = new Achievement();
        Long id = 3L;
        String name = "Sleep Master";
        String unlockCondition = "Sleep 8 hours for 7 nights";
        String emoji = "😴";
        String description = "Awarded for maintaining good sleep hygiene.";

        ach.setId(id);
        ach.setName(name);
        ach.setUnlockCondition(unlockCondition);
        ach.setEmoji(emoji);
        ach.setDescription(description);

        assertEquals(id, ach.getId());
        assertEquals(name, ach.getName());
        assertEquals(unlockCondition, ach.getUnlockCondition());
        assertEquals(emoji, ach.getEmoji());
        assertEquals(description, ach.getDescription());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(null, achievement);
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(achievement, achievement);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(achievement, new Object());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Achievement ach1 = Achievement.builder()
                .id(1L)
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .id(1L)
                .name("Different")
                .unlockCondition("Different condition")
                .emoji("🥇")
                .description("Different description")
                .build();

        assertEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithDifferentId() {
        Achievement ach1 = Achievement.builder()
                .id(1L)
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .id(2L)
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsSameFields() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        assertEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsDifferentName() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .name("Different")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsDifferentUnlockCondition() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Different condition")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsDifferentEmoji() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🥇")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsDifferentDescription() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        Achievement ach2 = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Different description")
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsAllNullFields() {
        Achievement ach1 = Achievement.builder()
                .name(null)
                .unlockCondition(null)
                .emoji(null)
                .description(null)
                .build();

        Achievement ach2 = Achievement.builder()
                .name(null)
                .unlockCondition(null)
                .emoji(null)
                .description(null)
                .build();

        assertEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFields() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition(null)
                .emoji("🏆")
                .description(null)
                .build();

        Achievement ach2 = Achievement.builder()
                .name("First Steps")
                .unlockCondition(null)
                .emoji("🏆")
                .description(null)
                .build();

        assertEquals(ach1, ach2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFieldsDifferent() {
        Achievement ach1 = Achievement.builder()
                .name("First Steps")
                .unlockCondition(null)
                .emoji("🏆")
                .description(null)
                .build();

        Achievement ach2 = Achievement.builder()
                .name("Different")
                .unlockCondition(null)
                .emoji("🏆")
                .description(null)
                .build();

        assertNotEquals(ach1, ach2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = achievement.hashCode();
        int hashCode2 = achievement.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEqualityForEqualObjects() {
        Achievement ach = Achievement.builder()
                .id(achievement.getId())
                .name(achievement.getName())
                .unlockCondition(achievement.getUnlockCondition())
                .emoji(achievement.getEmoji())
                .description(achievement.getDescription())
                .build();

        assertEquals(achievement.hashCode(), ach.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        Achievement ach = Achievement.builder()
                .id(null)
                .name(null)
                .unlockCondition(null)
                .emoji(null)
                .description(null)
                .build();

        int hashCode = Objects.hash(null, null, null, null, null);
        assertEquals(hashCode, ach.hashCode());
    }

    @Test
    void testToString() {
        String toString = achievement.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(achievement.getName()));
        assertTrue(toString.contains(achievement.getUnlockCondition()));
        assertTrue(toString.contains(achievement.getEmoji()));
        assertTrue(toString.contains(achievement.getDescription()));
    }

    @Test
    void testToStringWithNullFields() {
        Achievement ach = Achievement.builder()
                .id(null)
                .name(null)
                .unlockCondition(null)
                .emoji(null)
                .description(null)
                .build();

        String toString = ach.toString();
        assertNotNull(toString);
    }
}
