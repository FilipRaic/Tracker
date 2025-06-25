package hr.tvz.trackerplatform.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

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
