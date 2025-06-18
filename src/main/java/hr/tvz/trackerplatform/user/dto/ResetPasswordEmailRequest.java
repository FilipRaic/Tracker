package hr.tvz.trackerplatform.user.dto;

import jakarta.validation.constraints.Email;

public record ResetPasswordEmailRequest(
        @Email(message = "Email must be valid")
        String email
) {
}
