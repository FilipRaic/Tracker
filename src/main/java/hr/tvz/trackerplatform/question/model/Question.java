package hr.tvz.trackerplatform.question.model;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_generator")
    @SequenceGenerator(name = "question_generator", sequenceName = "seq_question_generator")
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return category == that.category && Objects.equals(content, that.content) &&
                Objects.equals(contentDe, that.contentDe) && Objects.equals(contentHr, that.contentHr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, content, contentDe, contentHr);
    }
}
