package hr.tvz.trackerplatform.daily_check.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DailyQuestionTest {

    private DailyQuestion dailyQuestion;

    @BeforeEach
    void setUp() {
        dailyQuestion = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("How are you feeling today?")
                .contentDe("Wie fühlst du dich heute?")
                .contentHr("Kako se osjećaš danas?")
                .score(5)
                .build();
    }

    @Test
    void testDefaultConstructor() {
        DailyQuestion question = new DailyQuestion();
        assertNull(question.getId());
        assertNull(question.getCategory());
        assertNull(question.getContent());
        assertNull(question.getContentDe());
        assertNull(question.getContentHr());
        assertNull(question.getScore());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        QuestionCategory category = QuestionCategory.PHYSICAL;
        String content = "Test question";
        String contentDe = "Testfrage";
        String contentHr = "Test pitanje";
        Integer score = 8;

        DailyQuestion question = new DailyQuestion(id, category, content, contentDe, contentHr, score);

        assertEquals(id, question.getId());
        assertEquals(category, question.getCategory());
        assertEquals(content, question.getContent());
        assertEquals(contentDe, question.getContentDe());
        assertEquals(contentHr, question.getContentHr());
        assertEquals(score, question.getScore());
    }

    @Test
    void testBuilder() {
        Long id = 2L;
        QuestionCategory category = QuestionCategory.MENTAL;
        String content = "How active were you today?";
        String contentDe = "Wie aktiv warst du heute?";
        String contentHr = "Koliko si bio aktivan danas?";
        Integer score = 7;

        DailyQuestion question = DailyQuestion.builder()
                .id(id)
                .category(category)
                .content(content)
                .contentDe(contentDe)
                .contentHr(contentHr)
                .score(score)
                .build();

        assertEquals(id, question.getId());
        assertEquals(category, question.getCategory());
        assertEquals(content, question.getContent());
        assertEquals(contentDe, question.getContentDe());
        assertEquals(contentHr, question.getContentHr());
        assertEquals(score, question.getScore());
    }

    @Test
    void testGettersAndSetters() {
        DailyQuestion question = new DailyQuestion();
        Long id = 3L;
        QuestionCategory category = QuestionCategory.EMOTIONAL;
        String content = "How are you today?";
        String contentDe = "Wie geht?";
        String contentHr = "Kako si?";
        Integer score = 6;

        question.setId(id);
        question.setCategory(category);
        question.setContent(content);
        question.setContentDe(contentDe);
        question.setContentHr(contentHr);
        question.setScore(score);

        assertEquals(id, question.getId());
        assertEquals(category, question.getCategory());
        assertEquals(content, question.getContent());
        assertEquals(contentDe, question.getContentDe());
        assertEquals(contentHr, question.getContentHr());
        assertEquals(score, question.getScore());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(null, dailyQuestion);
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(dailyQuestion, dailyQuestion);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(dailyQuestion, new Object());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        DailyQuestion question1 = DailyQuestion.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .id(1L)
                .category(QuestionCategory.MENTAL)
                .content("Different")
                .contentDe("Andere")
                .contentHr("Drugačiji")
                .score(3)
                .build();

        assertEquals(question1, question2);
    }

    @Test
    void testEqualsWithDifferentId() {
        DailyQuestion question1 = DailyQuestion.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .id(2L)
                .category(QuestionCategory.PHYSICAL)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsSameFields() {
        QuestionCategory category = QuestionCategory.SOCIAL;
        String content = "How did you socialize today?";
        String contentDe = "Wie hast du heute sozialisiert?";
        String contentHr = "Kako si se danas družio?";
        Integer score = 4;

        DailyQuestion question1 = DailyQuestion.builder()
                .category(category)
                .content(content)
                .contentDe(contentDe)
                .contentHr(contentHr)
                .score(score)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(category)
                .content(content)
                .contentDe(contentDe)
                .contentHr(contentHr)
                .score(score)
                .build();

        assertEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsDifferentCategory() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.MENTAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsDifferentContent() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Different")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsDifferentContentDe() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Andere")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsDifferentContentHr() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Drugačiji")
                .score(5)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsDifferentScore() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr("Sadržaj")
                .score(3)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsAllNullFields() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(null)
                .content(null)
                .contentDe(null)
                .contentHr(null)
                .score(null)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(null)
                .content(null)
                .contentDe(null)
                .contentHr(null)
                .score(null)
                .build();

        assertEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFields() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content(null)
                .contentDe("Inhalt")
                .contentHr(null)
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content(null)
                .contentDe("Inhalt")
                .contentHr(null)
                .score(5)
                .build();

        assertEquals(question1, question2);
    }

    @Test
    void testEqualsWithNullIdsSomeNullFieldsDifferent() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content(null)
                .contentDe("Inhalt")
                .contentHr(null)
                .score(5)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .content("Content")
                .contentDe("Inhalt")
                .contentHr(null)
                .score(5)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = dailyQuestion.hashCode();
        int hashCode2 = dailyQuestion.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEqualityForEqualObjects() {
        DailyQuestion question = DailyQuestion.builder()
                .category(dailyQuestion.getCategory())
                .content(dailyQuestion.getContent())
                .contentDe(dailyQuestion.getContentDe())
                .contentHr(dailyQuestion.getContentHr())
                .score(dailyQuestion.getScore())
                .build();

        assertEquals(dailyQuestion.hashCode(), question.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        DailyQuestion question = DailyQuestion.builder()
                .category(null)
                .content(null)
                .contentDe(null)
                .contentHr(null)
                .score(null)
                .build();

        int hashCode = Objects.hash(null, null, null, null, null);
        assertEquals(hashCode, question.hashCode());
    }

    @Test
    void testToString() {
        String toString = dailyQuestion.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(dailyQuestion.getCategory().toString()));
        assertTrue(toString.contains(dailyQuestion.getContent()));
        assertTrue(toString.contains(dailyQuestion.getContentDe()));
        assertTrue(toString.contains(dailyQuestion.getContentHr()));
        assertTrue(toString.contains(dailyQuestion.getScore().toString()));
    }

    @Test
    void testToStringWithNullFields() {
        DailyQuestion question = DailyQuestion.builder()
                .category(null)
                .content(null)
                .contentDe(null)
                .contentHr(null)
                .score(null)
                .build();

        String toString = question.toString();
        assertNotNull(toString);
    }

    @Test
    void testDifferentScoreValues() {
        DailyQuestion question1 = DailyQuestion.builder()
                .score(0)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .score(null)
                .build();

        assertNotEquals(question1, question2);
    }

    @Test
    void testDifferentCategoryValues() {
        DailyQuestion question1 = DailyQuestion.builder()
                .category(QuestionCategory.PHYSICAL)
                .build();

        DailyQuestion question2 = DailyQuestion.builder()
                .category(QuestionCategory.MENTAL)
                .build();

        assertNotEquals(question1, question2);
    }
}
