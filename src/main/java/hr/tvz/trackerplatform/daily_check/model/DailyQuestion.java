package hr.tvz.trackerplatform.daily_check.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyQuestion implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_question_generator")
    @SequenceGenerator(name = "daily_question_generator", sequenceName = "seq_daily_question_generator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "content_de", nullable = false)
    private String contentDe;

    @Column(name = "content_hr", nullable = false)
    private String contentHr;
    @Column(name = "score")
    private Integer score;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DailyQuestion that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(category, that.category) && Objects.equals(content, that.content) &&
                Objects.equals(contentDe, that.contentDe) && Objects.equals(contentHr, that.contentHr) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, content, contentDe, contentHr, score);
    }
}
