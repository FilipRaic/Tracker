package hr.tvz.trackerplatform.daily_check.model;

import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DailyCheckTest {

    private User user;
    private DailyCheck dailyCheck;
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
        assertThat(check.getUuid()).isNotNull();
        assertThat(check.getCheckInDate()).isNotNull();
        assertThat(check.getCreatedAt()).isNotNull();
        assertThat(check.getQuestions()).isNotNull();
        assertThat(check.isCompleted()).isFalse();
        assertThat(check.getId()).isNull();
        assertThat(check.getUser()).isNull();
        assertThat(check.getCompletedAt()).isNull();
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

        assertThat(check.getId()).isEqualTo(id);
        assertThat(check.getUuid()).isEqualTo(uuid);
        assertThat(check.getUser()).isEqualTo(user);
        assertThat(check.getCheckInDate()).isEqualTo(checkInDate);
        assertThat(check.getCreatedAt()).isEqualTo(createdAt);
        assertThat(check.getCompletedAt()).isEqualTo(completedAt);
        assertThat(check.getQuestions()).isEqualTo(questions);
        assertThat(check.isCompleted()).isTrue();
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

        assertThat(check.getUuid()).isEqualTo(uuid);
        assertThat(check.getUser()).isEqualTo(user);
        assertThat(check.getCheckInDate()).isEqualTo(checkInDate);
        assertThat(check.getCreatedAt()).isEqualTo(createdAt);
        assertThat(check.getQuestions()).isEqualTo(questions);
        assertThat(check.isCompleted()).isFalse();
        assertThat(check.getId()).isNull();
        assertThat(check.getCompletedAt()).isNull();
    }

    @Test
    void testSubmitResponses() {
        List<DailyQuestion> newQuestions = List.of(new DailyQuestion(), new DailyQuestion());
        dailyCheck.submitResponses(newQuestions);

        assertThat(dailyCheck.isCompleted()).isTrue();
        assertThat(dailyCheck.getCompletedAt()).isNotNull();
        assertThat(dailyCheck.getQuestions()).isEqualTo(newQuestions);
    }

    @Test
    void testSubmitResponsesWithEmptyList() {
        List<DailyQuestion> newQuestions = Collections.emptyList();
        dailyCheck.submitResponses(newQuestions);

        assertThat(dailyCheck.isCompleted()).isTrue();
        assertThat(dailyCheck.getCompletedAt()).isNotNull();
        assertThat(dailyCheck.getQuestions()).isEqualTo(newQuestions);
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
        assertThat(exception.getErrorMessage()).isEqualTo(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED);
    }

    @Test
    void testEqualsWithNull() {
        assertThat(dailyCheck).isNotEqualTo(null);
    }

    @Test
    void testEqualsWithSameObject() {
        assertThat(dailyCheck).isEqualTo(dailyCheck);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertThat(dailyCheck).isNotEqualTo(new Object());
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

        assertThat(check1).isEqualTo(check2);
    }

    @Test
    void testEqualsWithDifferentId() {
        DailyCheck check1 = DailyCheck.builder().id(1L).uuid(UUID.randomUUID()).build();
        DailyCheck check2 = DailyCheck.builder().id(2L).uuid(UUID.randomUUID()).build();

        assertThat(check1).isNotEqualTo(check2);
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

        assertThat(check1).isEqualTo(check2);
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

        assertThat(check1).isNotEqualTo(check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCheckInDate() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user).checkInDate(LocalDate.now())
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

        assertThat(check1).isNotEqualTo(check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCreatedAt() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user).checkInDate(LocalDate.now())
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

        assertThat(check1).isNotEqualTo(check2);
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
                .user(user).checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now().plusSeconds(1000))
                .questions(questions)
                .completed(true)
                .build();

        assertThat(check1).isNotEqualTo(check2);
    }

    @Test
    void testEqualsWithNullIdsDifferentQuestions() {
        DailyCheck check1 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user).checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(List.of(new DailyQuestion()))
                .completed(true)
                .build();
        DailyCheck check2 = DailyCheck.builder()
                .uuid(dailyCheck.getUuid())
                .user(user).checkInDate(LocalDate.now())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .questions(List.of(new DailyQuestion(), new DailyQuestion()))
                .completed(true)
                .build();

        assertThat(check1).isNotEqualTo(check2);
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

        assertThat(check1).isNotEqualTo(check2);
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

        assertThat(check1).isEqualTo(check2);
    }

    @Test
    @Disabled("This test is not working properly")
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

        assertThat(check1).isEqualTo(check2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = dailyCheck.hashCode();
        int hashCode2 = dailyCheck.hashCode();
        assertThat(hashCode1).isEqualTo(hashCode2);
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

        assertThat(check1).hasSameHashCodeAs(dailyCheck);
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
        assertThat(check.hashCode()).isEqualTo(hashCode);
    }

    @Test
    void testToString() {
        String toString = dailyCheck.toString();
        assertThat(toString)
                .isNotNull()
                .contains(dailyCheck.getUuid().toString())
                .contains(dailyCheck.getCheckInDate().toString());
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
        assertThat(toString).isNotNull();
    }
}
