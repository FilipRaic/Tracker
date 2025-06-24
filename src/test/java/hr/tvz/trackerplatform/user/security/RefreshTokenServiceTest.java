package hr.tvz.trackerplatform.user.security;

import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.RefreshToken;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.RefreshTokenRepository;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private final Long refreshTokenExpiration = 24 * 60 * 60 * 1000L; // 1 day in milliseconds
    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", refreshTokenExpiration);

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
    void findByUserId_shouldReturnRefreshToken() {
        Long userId = 1L;
        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        when(refreshTokenRepository.findByUserId(userId)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByUserId(userId);

        verify(refreshTokenRepository).findByUserId(userId);
        assertThat(result).isPresent().contains(refreshToken);
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenTokenNotFound() {
        Long userId = 1L;

        when(refreshTokenRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenService.findByUserId(userId);

        verify(refreshTokenRepository).findByUserId(userId);
        assertThat(result).isEmpty();
    }

    @Test
    void createRefreshToken_shouldCreateTokenSuccessfully() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        refreshTokenService.createRefreshToken(userId);

        verify(userRepository).findById(userId);
        verify(refreshTokenRepository).deleteAllByUserId(userId);

        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());

        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.getToken()).isNotNull();
        assertThat(savedToken.getExpiryDate()).isAfterOrEqualTo(Instant.now());
        assertThat(savedToken.getExpiryDate()).isBeforeOrEqualTo(Instant.now().plusMillis(refreshTokenExpiration + 1000));
    }

    @Test
    void createRefreshToken_shouldThrowException_whenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.createRefreshToken(userId))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(userRepository).findById(userId);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void verifyExpiration_shouldReturnToken_whenNotExpired() {
        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        RefreshToken result = refreshTokenService.verifyExpiration(refreshToken);

        assertThat(result).isEqualTo(refreshToken);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void verifyExpiration_shouldThrowException_andDeleteToken_whenExpired() {
        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().minusMillis(1000))
                .build();

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(refreshToken))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.REFRESH_TOKEN_EXPIRED.getMessage());

        verify(refreshTokenRepository).delete(refreshToken);
    }
}
