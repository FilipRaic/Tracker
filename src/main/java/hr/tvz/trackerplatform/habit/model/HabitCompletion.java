package hr.tvz.trackerplatform.habit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HabitCompletion {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_completion_generator")
    @SequenceGenerator(name = "habit_completion_generator", sequenceName = "seq_habit_completion_generator")
    private Long id;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    @Column(nullable = false)
    private Boolean done;

    @Column(nullable = false)
    private Integer streak;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HabitCompletion that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(completionDate, that.completionDate) && Objects.equals(done, that.done) &&
                Objects.equals(streak, that.streak) && Objects.equals(habit, that.habit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completionDate, done, streak, habit);
    }
}
