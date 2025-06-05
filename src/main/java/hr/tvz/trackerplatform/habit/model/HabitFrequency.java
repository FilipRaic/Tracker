package hr.tvz.trackerplatform.habit.model;

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
public class HabitFrequency {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_frequency_generator")
    @SequenceGenerator(name = "habit_frequency_generator", sequenceName = "seq_habit_frequency_generator")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HabitFrequency that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}



