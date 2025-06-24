package hr.tvz.trackerplatform.user.security;

import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private User user;
    private String secretKey;
    private long accessTokenExpiration;
    private long resetPasswordExpiration;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"; // 32-byte key for HS256
        accessTokenExpiration = 1000 * 60 * 60; // 1 hour in milliseconds
        resetPasswordExpiration = 1000 * 60 * 15; // 15 minutes in milliseconds

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", accessTokenExpiration);
        ReflectionTestUtils.setField(jwtService, "resetPasswordExpiration", resetPasswordExpiration);

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.USER)
                .password("encodedPassword")
                .build();
    }

    @Test
    void extractUsername_shouldReturnUsername() {
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(user.getEmail());
    }

    @Test
    void extractUsername_shouldReturnUsername_forExpiredToken() {
        // Generate a token with a short expiration time
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", -1000L); // Expired 1 second ago
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(user.getEmail());
    }

    @Test
    void extractClaim_shouldExtractClaim() {
        String token = jwtService.generateToken(user);

        Long id = jwtService.extractClaim(token, claims -> claims.get("id", Long.class));

        assertThat(id).isEqualTo(user.getId());
    }

    @Test
    void generateToken_shouldGenerateValidToken() {
        String token = jwtService.generateToken(user);

        Claims claims = parseToken(token);

        assertThat(claims.getSubject()).isEqualTo(user.getEmail());
        assertThat(claims.get("id", Long.class)).isEqualTo(user.getId());
        assertThat(claims.get("firstName", String.class)).isEqualTo(user.getFirstName());
        assertThat(claims.get("lastName", String.class)).isEqualTo(user.getLastName());
        assertThat(claims.get("email", String.class)).isEqualTo(user.getEmail());
        assertThat(claims.get("role", String.class)).isEqualTo(user.getRole().name());
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isAfterOrEqualTo(new Date(System.currentTimeMillis() + accessTokenExpiration - 1000));
        assertThat(claims.getExpiration()).isBeforeOrEqualTo(new Date(System.currentTimeMillis() + accessTokenExpiration + 1000));
    }

    @Test
    void generateResetPasswordToken_shouldGenerateValidToken() {
        String token = jwtService.generateResetPasswordToken(user);

        Claims claims = parseToken(token);

        assertThat(claims.getSubject()).isEqualTo(user.getEmail());
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isAfterOrEqualTo(new Date(System.currentTimeMillis() + resetPasswordExpiration - 1000));
        assertThat(claims.getExpiration()).isBeforeOrEqualTo(new Date(System.currentTimeMillis() + resetPasswordExpiration + 1000));
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {
        String token = jwtService.generateToken(user);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", -1000L); // Expired 1 second ago
        String token = jwtService.generateToken(user);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_forInvalidUsername() {
        String token = jwtService.generateToken(user);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("wrong@example.com")
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    private Claims parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
