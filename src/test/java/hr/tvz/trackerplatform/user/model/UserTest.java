package hr.tvz.trackerplatform.user.model;

import hr.tvz.trackerplatform.user.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testEquals_sameId_shouldBeEqual() {
        User user1 = createUser(100L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");
        User user2 = createUser(100L, "Jane", "Smith", "jane.smith@example.com", Role.ADMIN, "different");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testEquals_differentId_shouldNotBeEqual() {
        User user1 = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");
        User user2 = createUser(2L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void testEquals_nullId_sameEmail_shouldBeEqual() {
        User user1 = createUser(null, "John", "Doe", "john.doe@example.com", Role.USER, "password123");
        User user2 = createUser(null, "Jane", "Smith", "john.doe@example.com", Role.ADMIN, "different");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testEquals_nullId_differentEmail_shouldNotBeEqual() {
        User user1 = createUser(null, "John", "Doe", "john.doe@example.com", Role.USER, "password123");
        User user2 = createUser(null, "John", "Doe", "jane.smith@example.com", Role.USER, "password123");

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void testEquals_differentType_shouldReturnFalse() {
        User user = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");

        assertThat(user.equals("not a user")).isFalse();
    }

    @Test
    void testEquals_sameInstance_shouldReturnTrue() {
        User user = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");

        assertThat(user).isEqualTo(user);
    }

    @Test
    void testEquals_null_shouldReturnFalse() {
        User user = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");

        assertThat(user.equals(null)).isFalse();
    }

    @Test
    void testEquals_allFieldsNull_shouldBeEqual() {
        User user1 = createUser(null, null, null, null, null, null);
        User user2 = createUser(null, null, null, null, null, null);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testHashCode_consistency() {
        User user1 = createUser(null, "John", "Doe", "john.doe@example.com", Role.USER);
        User user2 = createUser(null, "John", "Doe", "john.doe@example.com", Role.USER);

        assertThat(user1).hasSameHashCodeAs(user2);
    }

    @Test
    void testToString_containsFields() {
        User user = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");
        String toString = user.toString();

        assertThat(toString)
                .contains("John")
                .contains("Doe")
                .contains("john.doe@example.com")
                .contains("USER");
    }

    @Test
    void testGettersSetters() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", Role.USER, "password123");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void testUpdatePassword_shouldUpdatePassword() {
        User user = createUser(1L, "John", "Doe", "john.doe@example.com", Role.USER, "oldPassword");
        String newPassword = "newPassword123";

        user.updatePassword(newPassword);

        assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    private User createUser(Long id, String firstName, String lastName, String email, Role role) {
        return createUser(id, firstName, lastName, email, role, "password123");
    }

    private User createUser(Long id, String firstName, String lastName, String email, Role role, String password) {
        return User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .password(password)
                .build();
    }
}
