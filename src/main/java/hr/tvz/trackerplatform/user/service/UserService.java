package hr.tvz.trackerplatform.user.service;

import hr.tvz.trackerplatform.user.dto.AuthResponse;
import hr.tvz.trackerplatform.user.dto.LoginRequest;
import hr.tvz.trackerplatform.user.dto.RegisterRequest;
import hr.tvz.trackerplatform.user.dto.ResetPasswordEmailRequest;
import hr.tvz.trackerplatform.user.dto.ResetPasswordRequest;

public interface UserService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshAccessToken(Long userId);
    void sendResetPasswordEmail(ResetPasswordEmailRequest request);
    void resetPassword(ResetPasswordRequest request, String email);
}
