package hr.tvz.trackerplatform.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    DAILY_CHECK_NOT_FOUND("Daily check not found", HttpStatus.BAD_REQUEST),
    DAILY_CHECK_ALREADY_SUBMITTED("Daily check already submitted", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
