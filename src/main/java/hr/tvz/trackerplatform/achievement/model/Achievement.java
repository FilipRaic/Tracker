package hr.tvz.trackerplatform.achievement.model;

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
public class Achievement {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "achievement_generator")
    @SequenceGenerator(name = "achievement_generator", sequenceName = "seq_achievement_generator")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unlock_condition", nullable = false)
    private String unlockCondition;

    @Column(name = "emoji")
    private String emoji;

    @Column(name = "description", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Achievement that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(name, that.name) && Objects.equals(unlockCondition, that.unlockCondition) &&
                Objects.equals(emoji, that.emoji) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unlockCondition, emoji, description);
    }
}
