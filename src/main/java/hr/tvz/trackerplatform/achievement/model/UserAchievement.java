package hr.tvz.trackerplatform.achievement.model;

import hr.tvz.trackerplatform.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity(name = "user_achievement")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievement {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_achievement_generator")
    @SequenceGenerator(name = "user_achievement_generator", sequenceName = "seq_user_achievement_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_achievement_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_achievement_achievement"))
    private Achievement achievement;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserAchievement that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return completed == that.completed && Objects.equals(user, that.user) && Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, achievement, completed);
    }
}
