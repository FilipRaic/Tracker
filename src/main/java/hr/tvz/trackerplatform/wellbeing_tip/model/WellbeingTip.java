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

    @Column(name = "tip_text_en")
    private String tipTextEn;

    @Column(name = "tip_text_hr")
    private String tipTextHr;

    @Column(name = "tip_text_de")
    private String tipTextDe;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WellbeingTip that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return category == that.category && Objects.equals(score, that.score) && Objects.equals(tipTextEn, that.tipTextEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, score, tipTextEn, tipTextHr, tipTextDe);
    }
}
