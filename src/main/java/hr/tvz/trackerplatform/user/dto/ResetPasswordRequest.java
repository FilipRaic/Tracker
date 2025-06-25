package hr.tvz.trackerplatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[!@#$%^&*]).*$",
                message = "Password must contain at least one number and one special character"
        )
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
    public boolean passwordsMatch() {
        return password.equals(confirmPassword);
    }
}
