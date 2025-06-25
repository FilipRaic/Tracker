package hr.tvz.trackerplatform.shared.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    DAILY_CHECK_NOT_FOUND("Daily check not found", HttpStatus.BAD_REQUEST),
    DAILY_CHECK_ALREADY_SUBMITTED("Daily check already submitted", HttpStatus.BAD_REQUEST),
    ERROR_GENERATING_EMAIL("Error generating email", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Invalid credentials", HttpStatus.UNAUTHORIZED),
    REQUIRE_AUTHENTICATION("Require authentication", HttpStatus.UNAUTHORIZED),
    PASSWORDS_DO_NOT_MATCH("Passwords do not match", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_WEAK("Password must contain at least one number and one special character", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND("Refresh token not found", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_EXPIRED("Refresh token expired", HttpStatus.UNAUTHORIZED),
    JOURNAL_ENTRY_NOT_FOUND("Journal entry not found", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
