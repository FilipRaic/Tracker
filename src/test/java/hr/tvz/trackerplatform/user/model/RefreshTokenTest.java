package hr.tvz.trackerplatform.user.model;

import hr.tvz.trackerplatform.user.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        User user = createUser(1L, "user@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(100L, "token123", user, expiryDate);
        RefreshToken token2 = createRefreshToken(100L, "different", user, expiryDate.plusSeconds(3600));

        assertThat(token1).isEqualTo(token2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(1L, "token123", user, expiryDate);
        RefreshToken token2 = createRefreshToken(2L, "token123", user, expiryDate);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldBeEqual() {
        User user = createUser(1L, "user@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(null, "token123", user, expiryDate);
        RefreshToken token2 = createRefreshToken(null, "token123", user, expiryDate);

        assertThat(token1).isEqualTo(token2);
    }

    @Test
    void testEquals_nullId_fieldComparison_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(null, "token123", user, expiryDate);
        RefreshToken token2 = createRefreshToken(null, "different", user, expiryDate);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now());

        assertThat(token.equals("not a refresh token")).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now());

        assertThat(token).isEqualTo(token);
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now());

        assertThat(token.equals(null)).isFalse();
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        RefreshToken token1 = createRefreshToken(null, null, null, null);
        RefreshToken token2 = createRefreshToken(null, null, null, null);

        assertThat(token1).isEqualTo(token2);
    }

    @Test
    void testEquals_differentUser_shouldNotBeEqual() {
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(null, "token123", createUser(1L, "user1@example.com"), expiryDate);
        RefreshToken token2 = createRefreshToken(null, "token123", createUser(2L, "user2@example.com"), expiryDate);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void testEquals_differentExpiryDate_shouldNotBeEqual() {
        User user = createUser(1L, "user@example.com");

        RefreshToken token1 = createRefreshToken(null, "token123", user, Instant.now());
        RefreshToken token2 = createRefreshToken(null, "token123", user, Instant.now().plusSeconds(3600));

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void testHashCode_consistency() {
        User user = createUser(1L, "user@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token1 = createRefreshToken(null, "token123", user, expiryDate);
        RefreshToken token2 = createRefreshToken(null, "token123", user, expiryDate);

        assertThat(token1).hasSameHashCodeAs(token2);
    }

    @Test
    void testToString_containsFields() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now());
        String toString = token.toString();

        assertThat(toString)
                .contains("token123")
                .contains(token.getExpiryDate().toString());
    }

    @Test
    void testGettersSetters() {
        User user = createUser(2L, "user2@example.com");
        Instant expiryDate = Instant.now();

        RefreshToken token = new RefreshToken(1L, "token123", user, expiryDate);

        assertThat(token.getId()).isEqualTo(1L);
        assertThat(token.getToken()).isEqualTo("token123");
        assertThat(token.getUser().getId()).isEqualTo(2L);
        assertThat(token.getExpiryDate()).isEqualTo(expiryDate);
    }

    @Test
    void testIsExpired_shouldReturnTrue_whenExpired() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now().minusSeconds(3600));

        assertThat(token.isExpired()).isTrue();
    }

    @Test
    void testIsExpired_shouldReturnFalse_whenNotExpired() {
        RefreshToken token = createRefreshToken(1L, "token123", createUser(1L, "user@example.com"), Instant.now().plusSeconds(3600));

        assertThat(token.isExpired()).isFalse();
    }

    private User createUser(Long id, String email) {
        return new User(id, "Test", "User", email, Role.USER, "TestPassword");
    }

    private RefreshToken createRefreshToken(Long id, String token, User user, Instant expiryDate) {
        return RefreshToken.builder()
                .id(id)
                .token(token)
                .user(user)
                .expiryDate(expiryDate)
                .build();
    }
}
