package hr.tvz.trackerplatform.user.service;

import hr.tvz.trackerplatform.achievement.service.AchievementService;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.service.EmailService;
import hr.tvz.trackerplatform.user.dto.*;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.RefreshToken;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.user.security.JwtService;
import hr.tvz.trackerplatform.user.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AchievementService achievementService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        validatePassword(request.password(), request.passwordsMatch());

        if (userRepository.existsByEmail(request.email())) {
            throw new TrackerException(ErrorMessage.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User createdUser = userRepository.save(user);
        achievementService.generateAchievementsForUser(createdUser);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (Exception _) {
            throw new TrackerException(ErrorMessage.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new TrackerException(ErrorMessage.USER_NOT_FOUND));

        String jwtToken = jwtService.generateToken(user);
        refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshAccessToken(Long userId) {
        return refreshTokenService.findByUserId(userId)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user ->
                        AuthResponse.builder()
                                .accessToken(jwtService.generateToken(user))
                                .build())
                .orElseThrow(() -> new TrackerException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public void sendResetPasswordEmail(ResetPasswordEmailRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new TrackerException(ErrorMessage.USER_NOT_FOUND));

        String token = jwtService.generateResetPasswordToken(user);

        emailService.sendResetPasswordEmail(user, token);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request, String email) {
        validatePassword(request.password(), request.passwordsMatch());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new TrackerException(ErrorMessage.USER_NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    private void validatePassword(String password, boolean passwordsMatch) {
        if (!passwordsMatch) {
            throw new TrackerException(ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }

        if (password.length() < 8) {
            throw new TrackerException(ErrorMessage.PASSWORD_TOO_SHORT);
        }

        if (!password.matches("^(?=.*\\d)(?=.*[!@#$%^&*]).*$")) {
            throw new TrackerException(ErrorMessage.PASSWORD_TOO_WEAK);
        }
    }
}
