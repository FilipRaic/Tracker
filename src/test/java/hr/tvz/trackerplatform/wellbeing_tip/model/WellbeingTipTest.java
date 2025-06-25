package hr.tvz.trackerplatform.wellbeing_tip.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WellbeingTipTest {

    @Test
    void builder_shouldCreateWellbeingTip_withAllFields() {
        WellbeingTip tip = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip.getId()).isEqualTo(1L);
        assertThat(tip.getCategory()).isEqualTo(QuestionCategory.PHYSICAL);
        assertThat(tip.getScore()).isEqualTo(10);
        assertThat(tip.getTipText()).isEqualTo("Drink more water");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyWellbeingTip() {
        WellbeingTip tip = new WellbeingTip();

        assertThat(tip.getId()).isNull();
        assertThat(tip.getCategory()).isNull();
        assertThat(tip.getScore()).isNull();
        assertThat(tip.getTipText()).isNull();
    }

    @Test
    void allArgsConstructor_shouldCreateWellbeingTip_withAllFields() {
        WellbeingTip tip = new WellbeingTip(1L, QuestionCategory.PHYSICAL, 10, "Drink more water");

        assertThat(tip.getId()).isEqualTo(1L);
        assertThat(tip.getCategory()).isEqualTo(QuestionCategory.PHYSICAL);
        assertThat(tip.getScore()).isEqualTo(10);
        assertThat(tip.getTipText()).isEqualTo("Drink more water");
    }

    @Test
    void settersAndGetters_shouldSetAndGetFieldsCorrectly() {
        WellbeingTip tip = new WellbeingTip();
        tip.setId(2L);
        tip.setCategory(QuestionCategory.MENTAL);
        tip.setScore(20);
        tip.setTipText("Practice mindfulness");

        assertThat(tip.getId()).isEqualTo(2L);
        assertThat(tip.getCategory()).isEqualTo(QuestionCategory.MENTAL);
        assertThat(tip.getScore()).isEqualTo(20);
        assertThat(tip.getTipText()).isEqualTo("Practice mindfulness");
    }

    @Test
    void toString_shouldReturnCorrectStringRepresentation() {
        WellbeingTip tip = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        String toString = tip.toString();

        assertThat(toString)
                .contains("id=1")
                .contains("category=PHYSICAL")
                .contains("score=10")
                .contains("tipText=Drink more water");
    }

    @Test
    void equals_shouldReturnTrue_forSameId() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.MENTAL)
                .score(20)
                .tipText("Practice mindfulness")
                .build();

        assertThat(tip1)
                .isEqualTo(tip2)
                .doesNotHaveSameHashCodeAs(tip2);
    }

    @Test
    void equals_shouldReturnTrue_forSameFieldsWhenIdsNull() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip1)
                .isEqualTo(tip2)
                .hasSameHashCodeAs(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentIds() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .id(2L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip1).isNotEqualTo(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentFieldsWhenIdsNull() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .category(QuestionCategory.MENTAL)
                .score(20)
                .tipText("Practice mindfulness")
                .build();

        assertThat(tip1).isNotEqualTo(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forNullOrDifferentType() {
        WellbeingTip tip = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip)
                .isNotEqualTo(null)
                .isNotEqualTo(new Object());
    }

    @Test
    void hashCode_shouldBeConsistent_forSameObject() {
        WellbeingTip tip = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        int hashCode1 = tip.hashCode();
        int hashCode2 = tip.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void hashCode_shouldBeDifferent_forDifferentIds() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .id(2L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip1.hashCode()).isNotEqualTo(tip2.hashCode());
    }

    @Test
    void equals_shouldReturnTrue_forSameObjectReference() {
        WellbeingTip tip = WellbeingTip.builder()
                .id(1L)
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip).isEqualTo(tip);
    }

    @Test
    void equals_shouldReturnTrue_forNullFieldsWhenIdsEqual() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .id(1L)
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .id(1L)
                .build();

        assertThat(tip1)
                .isEqualTo(tip2)
                .hasSameHashCodeAs(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentTipTextWhenIdsNull() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Exercise regularly")
                .build();

        assertThat(tip1).isNotEqualTo(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentScoreWhenIdsNull() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(20)
                .tipText("Drink more water")
                .build();

        assertThat(tip1).isNotEqualTo(tip2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentCategoryWhenIdsNull() {
        WellbeingTip tip1 = WellbeingTip.builder()
                .category(QuestionCategory.PHYSICAL)
                .score(10)
                .tipText("Drink more water")
                .build();
        WellbeingTip tip2 = WellbeingTip.builder()
                .category(QuestionCategory.SOCIAL)
                .score(10)
                .tipText("Drink more water")
                .build();

        assertThat(tip1).isNotEqualTo(tip2);
    }
}
