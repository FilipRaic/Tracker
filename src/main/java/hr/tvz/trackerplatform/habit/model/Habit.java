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
public class Habit {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_generator")
    @SequenceGenerator(name = "habit_generator", sequenceName = "seq_habit_generator")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate begin;

    private String description;

    @ManyToOne
    @JoinColumn(name = "frequency_id", nullable = false)
    private HabitFrequency habitFrequency;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Habit that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(name, that.name) && Objects.equals(begin, that.begin) &&
                Objects.equals(description, that.description) && Objects.equals(habitFrequency, that.habitFrequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, begin, description, habitFrequency);
    }
}
