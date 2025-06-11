package hr.tvz.trackerplatform.achievement.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="unlock_condition", nullable = false)
    private String unlockCondition;

    @Column(name="emoji")
    private String emoji;

    @Column(name="description", nullable = false)
    private String description;
}
