package hr.tvz.trackerplatform.daily_check.model;

import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"check_in_date"}, name = "uk_daily_check_date")},
        indexes = {@Index(columnList = "user_id", name = "IX_daily_check_user")}
)
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyCheck {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_check_generator")
    @SequenceGenerator(name = "daily_check_generator", sequenceName = "seq_daily_check_generator")
    private Long id;

    @Builder.Default
    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_daily_check_user"))
    private User user;

    @Builder.Default
    @Column(name = "check_in_date", nullable = false, unique = true)
    private LocalDate checkInDate = LocalDate.now();

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder.Default
    @OneToMany(targetEntity = DailyQuestion.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "daily_check_id", foreignKey = @ForeignKey(name = "fk_question_daily_check"))
    private List<DailyQuestion> questions = new ArrayList<>();

    @Builder.Default
    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DailyCheck that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return completed == that.completed && Objects.equals(uuid, that.uuid) &&
                Objects.equals(checkInDate, that.checkInDate) && Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(completedAt, that.completedAt) && Objects.equals(questions, that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, checkInDate, createdAt, completedAt, questions, completed);
    }

    public void submitResponses(List<DailyQuestion> dailyQuestions) {
        if (this.completed) {
            throw new TrackerException(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED);
        }

        this.questions = new ArrayList<>(dailyQuestions);
        this.completedAt = LocalDateTime.now();
        this.completed = true;
    }
}
