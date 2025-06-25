package hr.tvz.trackerplatform.question.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        Question q1 = createQuestion(100L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(100L, QuestionCategory.EMOTIONAL, "How do you feel?", "Wie fühlst du dich?", "Kako se osjećaš?");

        assertThat(q1).isEqualTo(q2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        Question q1 = createQuestion(1L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(2L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(q1).isNotEqualTo(q2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(q1).isEqualTo(q2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.MENTAL, "Different", "Wie geht's?", "Kako si?");

        assertThat(q1).isNotEqualTo(q2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        Question question = createQuestion(1L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(question.equals("not a question")).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        Question question = createQuestion(1L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(question).isEqualTo(question);
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        Question question = createQuestion(1L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(question.equals(null)).isFalse();
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        Question q1 = createQuestion(null, null, null, null, null);
        Question q2 = createQuestion(null, null, null, null, null);

        assertThat(q1).isEqualTo(q2);
    }

    @Test
    void testEquals_differentCategory_shouldNotBeEqual() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.EMOTIONAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(q1).isNotEqualTo(q2);
    }

    @Test
    void testEquals_differentContentDe_shouldNotBeEqual() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Different", "Kako si?");

        assertThat(q1).isNotEqualTo(q2);
    }

    @Test
    void testEquals_differentContentHr_shouldNotBeEqual() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Different");

        assertThat(q1).isNotEqualTo(q2);
    }

    @Test
    void testHashCode_consistency() {
        Question q1 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        Question q2 = createQuestion(null, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");

        assertThat(q1).hasSameHashCodeAs(q2);
    }

    @Test
    void testToString_containsFields() {
        Question question = createQuestion(1L, QuestionCategory.MENTAL, "How are you?", "Wie geht's?", "Kako si?");
        String toString = question.toString();

        assertThat(toString)
                .contains("MENTAL")
                .contains("How are you?")
                .contains("Wie geht's?")
                .contains("Kako si?");
    }

    @Test
    void testGettersSetters() {
        Question question = new Question(1L, QuestionCategory.EMOTIONAL, "How do you feel?",
                "Wie fühlst du dich?", "Kako se osjećaš?");

        assertThat(question.getId()).isEqualTo(1L);
        assertThat(question.getCategory()).isEqualTo(QuestionCategory.EMOTIONAL);
        assertThat(question.getContent()).isEqualTo("How do you feel?");
        assertThat(question.getContentDe()).isEqualTo("Wie fühlst du dich?");
        assertThat(question.getContentHr()).isEqualTo("Kako se osjećaš?");
    }

    private Question createQuestion(Long id, QuestionCategory category, String content, String contentDe, String contentHr) {
        return Question.builder()
                .id(id)
                .category(category)
                .content(content)
                .contentDe(contentDe)
                .contentHr(contentHr)
                .build();
    }
}
