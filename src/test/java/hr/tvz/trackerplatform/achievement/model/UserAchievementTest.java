package hr.tvz.trackerplatform.achievement.model;

import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserAchievementTest {

    private User user;
    private Achievement achievement;
    private UserAchievement userAchievement;

    @BeforeEach
    void setUp() {
        user = new User();
        achievement = Achievement.builder()
                .name("First Steps")
                .unlockCondition("Complete 5 daily checks")
                .emoji("🏆")
                .description("Awarded for completing your first 5 daily checks.")
                .build();

        userAchievement = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();
    }

    @Test
    void testDefaultConstructor() {
        UserAchievement ua = new UserAchievement();
        assertNull(ua.getId());
        assertNull(ua.getUser());
        assertNull(ua.getAchievement());
        assertFalse(ua.isCompleted());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        user = new User();
        achievement = Achievement.builder()
                .name("Marathon Runner")
                .unlockCondition("Run 10km in a week")
                .emoji("🏃")
                .description("Awarded for running 10 kilometers in a single week.")
                .build();
        boolean completed = false;

        userAchievement = new UserAchievement(id, user, achievement, completed);

        assertEquals(id, userAchievement.getId());
        assertEquals(user, userAchievement.getUser());
        assertEquals(achievement, userAchievement.getAchievement());
        assertEquals(completed, userAchievement.isCompleted());
    }

    @Test
    void testBuilder() {
        Long id = 2L;
        user = new User();
        achievement = Achievement.builder()
                .name("Healthy Eater")
                .unlockCondition("Log 7 healthy meals")
                .emoji("🥗")
                .description("Awarded for logging 7 healthy meals in a row.")
                .build();
        boolean completed = true;

        userAchievement = UserAchievement.builder()
                .id(id)
                .user(user)
                .achievement(achievement)
                .completed(completed)
                .build();

        assertEquals(id, userAchievement.getId());
        assertEquals(user, userAchievement.getUser());
        assertEquals(achievement, userAchievement.getAchievement());
        assertEquals(completed, userAchievement.isCompleted());
    }

    @Test
    void testGettersAndSetters() {
        Long id = 3L;
        user = new User();
        achievement = Achievement.builder()
                .name("Sleep Master")
                .unlockCondition("Sleep 8 hours for 7 nights")
                .emoji("😴")
                .description("Awarded for maintaining good sleep hygiene.")
                .build();
        boolean completed = false;

        userAchievement.setId(id);
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setCompleted(completed);

        assertEquals(id, userAchievement.getId());
        assertEquals(user, userAchievement.getUser());
        assertEquals(achievement, userAchievement.getAchievement());
        assertEquals(completed, userAchievement.isCompleted());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(null, userAchievement);
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(userAchievement, userAchievement);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(userAchievement, new Object());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        UserAchievement ua1 = UserAchievement.builder()
                .id(1L)
                .user(new User())
                .achievement(Achievement.builder()
                        .name("First Steps")
                        .unlockCondition("Complete 5 daily checks")
                        .emoji("🏆")
                        .description("Awarded for completing your first 5 daily checks.")
                        .build())
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .id(1L)
                .user(new User())
                .achievement(Achievement.builder()
                        .name("Different")
                        .unlockCondition("Different condition")
                        .emoji("🥇")
                        .description("Different description")
                        .build())
                .completed(false)
                .build();

        assertEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithDifferentId() {
        UserAchievement ua1 = UserAchievement.builder()
                .id(1L)
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .id(2L)
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        assertNotEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithNullIdsSameFields() {
        UserAchievement ua1 = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        assertEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithNullIdsDifferentAchievement() {
        UserAchievement ua1 = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .user(user)
                .achievement(Achievement.builder()
                        .name("Different")
                        .unlockCondition("Different condition")
                        .emoji("🥇")
                        .description("Different description")
                        .build())
                .completed(true)
                .build();

        assertNotEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCompleted() {
        UserAchievement ua1 = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .user(user)
                .achievement(achievement)
                .completed(false)
                .build();

        assertNotEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithNullIdsAllNullFields() {
        UserAchievement ua1 = UserAchievement.builder()
                .user(null)
                .achievement(null)
                .completed(false)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .user(null)
                .achievement(null)
                .completed(false)
                .build();

        assertEquals(ua1, ua2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFields() {
        UserAchievement ua1 = UserAchievement.builder()
                .user(user)
                .achievement(null)
                .completed(true)
                .build();

        UserAchievement ua2 = UserAchievement.builder()
                .user(user)
                .achievement(null)
                .completed(true)
                .build();

        assertEquals(ua1, ua2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = userAchievement.hashCode();
        int hashCode2 = userAchievement.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEqualityForEqualObjects() {
        UserAchievement ua = UserAchievement.builder()
                .id(userAchievement.getId())
                .user(userAchievement.getUser())
                .achievement(userAchievement.getAchievement())
                .completed(userAchievement.isCompleted())
                .build();

        assertEquals(userAchievement.hashCode(), ua.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        UserAchievement ua = UserAchievement.builder()
                .id(null)
                .user(null)
                .achievement(null)
                .completed(false)
                .build();

        int hashCode = Objects.hash(null, null, null, false);
        assertEquals(hashCode, ua.hashCode());
    }

    @Test
    void testToString() {
        String toString = userAchievement.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(String.valueOf(userAchievement.isCompleted())));
    }

    @Test
    void testToStringWithNullFields() {
        UserAchievement ua = UserAchievement.builder()
                .id(null)
                .user(null)
                .achievement(null)
                .completed(false)
                .build();

        String toString = ua.toString();
        assertNotNull(toString);
    }
}
