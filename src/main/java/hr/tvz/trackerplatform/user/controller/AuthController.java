package hr.tvz.trackerplatform.user.controller;

import hr.tvz.trackerplatform.user.dto.*;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import hr.tvz.trackerplatform.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserSecurity userSecurity;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/refresh-token")
    @PreAuthorize("@userSecurity.isCurrentUser()")
    public ResponseEntity<AuthResponse> refreshToken() {
        Long userId = userSecurity.getCurrentUserId();

        return ResponseEntity.ok(userService.refreshAccessToken(userId));
    }

    @PostMapping("/forgot-password/send")
    public ResponseEntity<Void> sendResetPasswordEmail(@Valid @RequestBody ResetPasswordEmailRequest request) {
        userService.sendResetPasswordEmail(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @PreAuthorize("@userSecurity.isCurrentUser()")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String email = userSecurity.getCurrentUserEmail();
        userService.resetPassword(request, email);

        return ResponseEntity.ok().build();
    }
}
