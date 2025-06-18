package hr.tvz.trackerplatform.wellbeing_tip.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WellbeingTip {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wellbeing_tip_generator")
    @SequenceGenerator(name = "wellbeing_tip_generator", sequenceName = "seq_wellbeing_tip_generator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Column(name = "score")
    private Integer score;

    @Column(name="tip_text")
    private String tipText;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof WellbeingTip that)) return false;
        return Objects.equals(id, that.id) && category == that.category && Objects.equals(score, that.score) && Objects.equals(tipText, that.tipText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, score, tipText);
    }
}
