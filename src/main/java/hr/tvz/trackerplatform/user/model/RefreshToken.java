package hr.tvz.trackerplatform.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"token"}, name = "uk_refresh_token_token")},
        indexes = {@Index(columnList = "token", name = "ix_refresh_token_token")}
)
@Getter
@Builder
@ToString(exclude = {"user"})
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_generator")
    @SequenceGenerator(name = "refresh_token_generator", sequenceName = "seq_refresh_token_generator")
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_refresh_token_user"))
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RefreshToken that))
            return false;

        if (Objects.nonNull(id) && Objects.nonNull(that.getId()))
            return id.equals(that.getId());

        return Objects.equals(token, that.token) && Objects.equals(user, that.user) && Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, user, expiryDate);
    }

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }
}
