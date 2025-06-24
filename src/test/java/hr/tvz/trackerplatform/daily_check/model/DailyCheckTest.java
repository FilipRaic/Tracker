package hr.tvz.trackerplatform.daily_check.model;

import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DailyCheckTest {

    private DailyCheck dailyCheck;
    private User user;
    private List<DailyQuestion> questions;

    @BeforeEach
    void setUp() {
        user = new User();
        questions = new ArrayList<>();
        dailyCheck = DailyCheck.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .questions(questions)
                .completed(false)
                .build();
    }

    @Test
    void testDefaultConstructor() {
        DailyCheck check = new DailyCheck();
        assertNotNull(check.getUuid());
        assertNotNull(check.getCheckInDate());
        assertNotNull(check.getCreatedAt());
        assertNotNull(check.getQuestions());
        assertFalse(check.isCompleted());
        assertNull(check.getId());
        assertNull(check.getUser());
        assertNull(check.getCompletedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID uuid = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        Instant createdAt = Instant.now();
        Instant completedAt = Instant.now();
        Long id = 1L;
        boolean completed = true;

        DailyCheck check = new DailyCheck(id, uuid, user, checkInDate, createdAt, completedAt, questions, completed);

        assertEquals(id, check.getId());
        assertEquals(uuid, check.getUuid());
        assertEquals(user, check.getUser());
        assertEquals(checkInDate, check.getCheckInDate());
        assertEquals(createdAt, check.getCreatedAt());
        assertEquals(completedAt, check.getCompletedAt());
        assertEquals(questions, check.getQuestions());
        assertTrue(check.isCompleted());
    }

    @Test
    void testBuilder() {
        UUID uuid = UUID.randomUUID();
        LocalDate checkInDate = LocalDate.now();
        Instant createdAt = Instant.now();

        DailyCheck check = DailyCheck.builder()
                .uuid(uuid)
                .user(user)
                .checkInDate(checkInDate)
                .createdAt(createdAt)
                .questions(questions)
                .completed(false)
                .build();

        assertEquals(uuid, check.getUuid());
        assertEquals(user, check.getUser());
        assertEquals(checkInDate, check.getCheckInDate());
        assertEquals(createdAt, check.getCreatedAt());
        assertEquals(questions, check.getQuestions());
        assertFalse(check.isCompleted());
        assertNull(check.getId());
        assertNull(check.getCompletedAt());
    }

    @Test
    void testSubmitResponses() {
        List<DailyQuestion> newQuestions = List.of(new DailyQuestion(), new DailyQuestion());
        dailyCheck.submitResponses(newQuestions);

        assertTrue(dailyCheck.isCompleted());
        assertNotNull(dailyCheck.getCompletedAt());
        assertEquals(newQuestions, dailyCheck.getQuestions());
    }

    @Test
    void testSubmitResponsesWithEmptyList() {
        List<DailyQuestion> newQuestions = Collections.emptyList();
        dailyCheck.submitResponses(newQuestions);

        assertTrue(dailyCheck.isCompleted());
        assertNotNull(dailyCheck.getCompletedAt());
        assertEquals(newQuestions, dailyCheck.getQuestions());
    }

    @Test
    void testSubmitResponsesWithNullList() {
        assertThrows(NullPointerException.class, () -> dailyCheck.submitResponses(null));
    }

    @Test
    void testSubmitResponsesWhenAlreadyCompleted() {
        dailyCheck.setCompleted(true);
        List<DailyQuestion> newQuestions = List.of(new DailyQuestion());

        TrackerException exception = assertThrows(TrackerException.class, () ->
                dailyCheck.submitResponses(newQuestions));
        assertEquals(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED, exception.getErrorMessage());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(dailyCheck, null);
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(dailyCheck, dailyCheck);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(dailyCheck, new Object());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        DailyCheck check1 = DailyCheck.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .user(new User())
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(new ArrayList<>())
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .user(new User())
                .checkInDate(LocalDate.now().plusDays(1))
                .createdAt(Instant.now().plusSeconds(1000))
                .completedAt(Instant.now().plusSeconds(2000))
                .questions(List.of(new DailyQuestion()))
                .completed(false)
                .build();

        assertEquals(check1, check2);
    }

    @Test
    void testEqualsWithDifferentId() {
        DailyCheck check1 = DailyCheck.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .id(2L)
                .uuid(UUID.randomUUID())
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsSameFields() {
        UUID uuid = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        Instant createdAt = Instant.now();
        Instant completedAt = Instant.now();
        boolean completed = true;

        DailyCheck check1 = DailyCheck.builder()
                .uuid(uuid)
                .user(user)
                .checkInDate(date)
                .createdAt(createdAt)
                .completedAt(completedAt)
                .questions(questions)
                .completed(completed)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(uuid)
                .user(user)
                .checkInDate(date)
                .createdAt(createdAt)
                .completedAt(completedAt)
                .questions(questions)
                .completed(completed)
                .build();

        assertEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentUuid() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCheckInDate() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now().plusDays(1))
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCreatedAt() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now().plusSeconds(1000))
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCompletedAt() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now().plusSeconds(1000))
                .questions(questions)
                .completed(true)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentQuestions() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(List.of(new DailyQuestion()))
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(List.of(new DailyQuestion(), new DailyQuestion()))
                .completed(true)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCompleted() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(questions)
                .completed(false)
                .build();

        assertNotEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsAllNullFields() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(null)
                .user(null)
                .checkInDate(null)
                .createdAt(null)
                .completedAt(null)
                .questions(null)
                .completed(false)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(null)
                .user(null)
                .checkInDate(null)
                .createdAt(null)
                .completedAt(null)
                .questions(null)
                .completed(false)
                .build();

        assertEquals(check1, check2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFields() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(null)
                .checkInDate(LocalDate.now())
                .createdAt(null)
                .completedAt(Instant.now())
                .questions(null)
                .completed(true)
                .build();

        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(null)
                .checkInDate(LocalDate.now())
                .createdAt(null)
                .completedAt(Instant.now())
                .questions(null)
                .completed(true)
                .build();

        assertEquals(check1, check2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = dailyCheck.hashCode();
        int hashCode2 = dailyCheck.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEqualityForEqualObjects() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user)
                .checkInDate(dailyCheck.getCheckInDate())
                .createdAt(dailyCheck.getCreatedAt())
                .completedAt(dailyCheck.getCompletedAt())
                .questions(dailyCheck.getQuestions())
                .completed(dailyCheck.isCompleted())
                .build();

        assertEquals(dailyCheck.hashCode(), check1.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        DailyCheck check = DailyCheck.builder()
                .uuid(null)
                .checkInDate(null)
                .createdAt(null)
                .completedAt(null)
                .questions(null)
                .completed(false)
                .build();

        int hashCode = Objects.hash(null, null, null, null, null, false);
        assertEquals(hashCode, check.hashCode());
    }

    @Test
    void testToString() {
        String toString = dailyCheck.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(dailyCheck.getUuid().toString()));
        assertTrue(toString.contains(dailyCheck.getCheckInDate().toString()));
    }

    @Test
    void testToStringWithNullFields() {
        DailyCheck check = DailyCheck.builder()
                .uuid(null)
                .user(null)
                .checkInDate(null)
                .createdAt(null)
                .completedAt(null)
                .questions(null)
                .completed(false)
                .build();

        String toString = check.toString();
        assertNotNull(toString);
    }
}
