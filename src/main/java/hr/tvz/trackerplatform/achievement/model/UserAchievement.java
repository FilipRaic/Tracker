package hr.tvz.trackerplatform.achievement.model;

import hr.tvz.trackerplatform.user.model.User;
import jakarta.persistence.*;
import lombok.*;

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
}
