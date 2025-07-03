package hr.tvz.trackerplatform.wellbeing_tip.dto;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WellbeingTipDTO {

    private QuestionCategory category;

    @Column(name = "score")
    private Integer score;

    @Column(name = "tip_text_en")
    private String tipTextEn;

    @Column(name = "tip_text_hr")
    private String tipTextHr;

    @Column(name = "tip_text_de")
    private String tipTextDe;
}
