package hr.tvz.trackerplatform.user.service;

import hr.tvz.trackerplatform.achievement.service.AchievementService;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.service.EmailServiceImpl;
import hr.tvz.trackerplatform.user.dto.*;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.RefreshToken;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.user.security.JwtService;
import hr.tvz.trackerplatform.user.security.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AchievementService achievementService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!", "Password123!");
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        verify(achievementService).generateAchievementsForUser(user);

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!", "Password123!");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());

        verifyNoInteractions(passwordEncoder, achievementService);
    }

    @Test
    void register_shouldThrowException_whenPasswordsDoNotMatch() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!", "Different123!");

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORDS_DO_NOT_MATCH.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder, achievementService);
    }

    @Test
    void register_shouldThrowException_whenPasswordTooShort() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Short!", "Short!");

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORD_TOO_SHORT.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder, achievementService);
    }

    @Test
    void register_shouldThrowException_whenPasswordTooWeak() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123", "password123");

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORD_TOO_WEAK.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder, achievementService);
    }

    @Test
    void login_shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("john.doe@example.com", "password123");
        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("encodedPassword")
                .build();
        String jwtToken = "jwt-token";

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        doNothing().when(refreshTokenService).createRefreshToken(user.getId());

        AuthResponse response = userService.login(request);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        verify(userRepository).findByEmail(request.email());
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).createRefreshToken(user.getId());

        assertThat(response.accessToken()).isEqualTo(jwtToken);
    }

    @Test
    void login_shouldThrowException_whenInvalidCredentials() {
        LoginRequest request = new LoginRequest("john.doe@example.com", "wrongPassword");

        doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.INVALID_CREDENTIALS.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userRepository, jwtService, refreshTokenService);
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginRequest request = new LoginRequest("john.doe@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.email());
        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void refreshAccessToken_shouldRefreshSuccessfully() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("john.doe@example.com")
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .build();
        String jwtToken = "new-jwt-token";

        when(refreshTokenService.findByUserId(userId)).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthResponse response = userService.refreshAccessToken(userId);

        verify(refreshTokenService).findByUserId(userId);
        verify(refreshTokenService).verifyExpiration(refreshToken);
        verify(jwtService).generateToken(user);

        assertThat(response.accessToken()).isEqualTo(jwtToken);
    }

    @Test
    void refreshAccessToken_shouldThrowException_whenRefreshTokenNotFound() {
        Long userId = 1L;

        when(refreshTokenService.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.refreshAccessToken(userId))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.REFRESH_TOKEN_NOT_FOUND.getMessage());

        verify(refreshTokenService).findByUserId(userId);
        verifyNoMoreInteractions(refreshTokenService);
        verifyNoInteractions(jwtService);
    }

    @Test
    void sendResetPasswordEmail_shouldSendEmailSuccessfully() {
        ResetPasswordEmailRequest request = new ResetPasswordEmailRequest("john.doe@example.com");
        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .build();
        String resetToken = "reset-token";

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateResetPasswordToken(user)).thenReturn(resetToken);

        userService.sendResetPasswordEmail(request);

        verify(userRepository).findByEmail(request.email());
        verify(jwtService).generateResetPasswordToken(user);
        verify(emailService).sendResetPasswordEmail(user, resetToken);
    }

    @Test
    void sendResetPasswordEmail_shouldThrowException_whenUserNotFound() {
        ResetPasswordEmailRequest request = new ResetPasswordEmailRequest("john.doe@example.com");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.sendResetPasswordEmail(request))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(userRepository).findByEmail(request.email());
        verifyNoInteractions(jwtService, emailService);
    }

    @Test
    void resetPassword_shouldResetPasswordSuccessfully() {
        String email = "john.doe@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "NewPassword123!");
        User user = User.builder()
                .id(1L)
                .email(email)
                .password("oldPassword")
                .build();
        String encodedPassword = "encodedNewPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(request, email);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(user);

        assertThat(user.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void resetPassword_shouldThrowException_whenUserNotFound() {
        String email = "john.doe@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "NewPassword123!");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.resetPassword(request, email))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void resetPassword_shouldThrowException_whenPasswordsDoNotMatch() {
        String email = "john.doe@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "Different123!");

        assertThatThrownBy(() -> userService.resetPassword(request, email))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORDS_DO_NOT_MATCH.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void resetPassword_shouldThrowException_whenPasswordTooShort() {
        String email = "john.doe@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest("Short!", "Short!");

        assertThatThrownBy(() -> userService.resetPassword(request, email))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORD_TOO_SHORT.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void resetPassword_shouldThrowException_whenPasswordTooWeak() {
        String email = "john.doe@example.com";
        ResetPasswordRequest request = new ResetPasswordRequest("password123", "password123");

        assertThatThrownBy(() -> userService.resetPassword(request, email))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.PASSWORD_TOO_WEAK.getMessage());

        verifyNoInteractions(userRepository, passwordEncoder);
    }
}
